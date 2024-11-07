package com.nhnacademy.heukbaekbookshop.book.service.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import com.nhnacademy.heukbaekbookshop.book.domain.BookStatus;
import com.nhnacademy.heukbaekbookshop.book.domain.BookTag;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookCreateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookUpdateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.*;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookSearchException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
import com.nhnacademy.heukbaekbookshop.common.formatter.BookFormatter;
import com.nhnacademy.heukbaekbookshop.contributor.domain.*;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorSummaryResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherSummaryResponse;
import com.nhnacademy.heukbaekbookshop.contributor.repository.ContributorRepository;
import com.nhnacademy.heukbaekbookshop.contributor.repository.PublisherRepository;
import com.nhnacademy.heukbaekbookshop.contributor.repository.RoleRepository;
import com.nhnacademy.heukbaekbookshop.image.domain.BookImage;
import com.nhnacademy.heukbaekbookshop.image.domain.Image;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.nhnacademy.heukbaekbookshop.image.repository.BookImageRepository;
import com.nhnacademy.heukbaekbookshop.image.repository.ImageRepository;
import com.nhnacademy.heukbaekbookshop.tag.domain.Tag;
import com.nhnacademy.heukbaekbookshop.tag.repository.TagRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookService {

    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final ContributorRepository contributorRepository;
    private final RoleRepository roleRepository;
    private final ImageRepository imageRepository;
    private final BookImageRepository bookImageRepository;
    private final TagRepository tagRepository;
    private final BookFormatter bookFormatter;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${aladin.api.key}")
    private String aladinApiKey;

    private static final int MAX_CATEGORY_COUNT = 10;

    public List<BookSearchResponse> searchBook(String title) {
        String url = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx" +
                "?ttbkey=" + aladinApiKey +
                "&Query=" + title +
                "&QueryType=Title" +
                "&MaxResults=10" +
                "&start=1" +
                "&SearchTarget=Book" +
                "&output=js" +
                "&Version=20131101";

        try {
            BookSearchApiResponse apiResponse = restTemplate.getForObject(url, BookSearchApiResponse.class);

            if (apiResponse == null || apiResponse.getItems() == null) {
                return List.of();
            }

            return apiResponse.getItems().stream()
                    .map(this::mapToBookSearchResponse)
                    .collect(Collectors.toList());
        } catch (RestClientException e) {
            throw new BookSearchException("Error occurred while calling the Aladin API.", e);
        }
    }

    @Transactional
    public BookCreateResponse registerBook(BookCreateRequest request) {
        if (bookRepository.findByIsbn(request.isbn()).isPresent()) {
            throw new BookAlreadyExistsException(request.isbn());
        }

        Publisher publisher = publisherRepository.findByName(request.publisher())
                .orElseGet(() -> {
                    Publisher newPublisher = new Publisher();
                    newPublisher.setName(request.publisher());
                    return publisherRepository.save(newPublisher);
                });

        Book book = new Book();
        book.setTitle(request.title());
        book.setIndex(request.index());
        book.setDescription(request.description());
        book.setPublication(Date.valueOf(String.valueOf(request.publication())));
        book.setIsbn(request.isbn());
        book.setPrice(BigDecimal.valueOf(request.standardPrice()));
        book.setDiscountRate(request.discountRate());
        book.setPackable(request.isPackable());
        book.setPublisher(publisher);
        book.setStatus(BookStatus.IN_STOCK);
        book.setStock(request.stock());
        book.setCategories(new HashSet<>());
        book.setContributors(new HashSet<>());

        bookRepository.save(book);

        Category leafCategory = processCategoryHierarchy(request.categories());
        if (leafCategory != null) {
            BookCategory bookCategory = new BookCategory(book, leafCategory);
            book.addCategory(bookCategory);
            leafCategory.addBookCategory(bookCategory);
        }

        List<ParsedPerson> parsedPersons = parseAuthors(request.authors());
        for (ParsedPerson person : parsedPersons) {
            Contributor contributor = contributorRepository.findByName(person.name)
                    .orElseGet(() -> {
                        Contributor newContributor = new Contributor();
                        newContributor.setName(person.name);
                        newContributor.setDescription("");
                        return contributorRepository.save(newContributor);
                    });

            ContributorRole contributorRole = switch (person.role) {
                case "지은이" -> ContributorRole.AUTHOR;
                case "옮긴이" -> ContributorRole.TRANSLATOR;
                default -> ContributorRole.EDITOR;
            };

            Role role = roleRepository.findByRoleName(contributorRole)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setRoleName(contributorRole);
                        return roleRepository.save(newRole);
                    });

            BookContributor bookContributor = new BookContributor();
            bookContributor.setBook(book);
            bookContributor.setContributor(contributor);
            bookContributor.setRole(role);
            book.getContributors().add(bookContributor);
        }

        if (request.imageUrl() != null && !request.imageUrl().trim().isEmpty()) {
            Image image = new Image();
            image.setUrl(request.imageUrl().trim());
            imageRepository.save(image);
            entityManager.flush();

            BookImage bookImage = new BookImage();
            bookImage.setBook(book);

            book.addBookImage(bookImage);

            bookImageRepository.save(bookImage);
        }

        bookRepository.save(book);

        return new BookCreateResponse(
                book.getTitle(),
                book.getIndex(),
                book.getDescription(),
                book.getPublication().toString(),
                book.getIsbn(),
                book.isPackable(),
                book.getStock(),
                book.getPrice().intValue(),
                book.getDiscountRate(),
                book.getPublisher().getName(),
                book.getCategories().stream()
                        .map(bc -> bc.getCategory().getName())
                        .collect(Collectors.toList()),
                book.getContributors().stream()
                        .map(bc -> bc.getContributor().getName())
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public BookUpdateResponse updateBook(Long bookId, BookUpdateRequest request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        book.setTitle(request.title());
        book.setIndex(request.index());
        book.setDescription(request.description());
        book.setPublication(Date.valueOf(request.publication()));
        book.setIsbn(request.isbn());
        book.setPackable(request.isPackable());
        book.setStock(request.stock());
        book.setPrice(BigDecimal.valueOf(request.standardPrice()));
        book.setDiscountRate(request.discountRate());
        book.setStatus(BookStatus.valueOf(request.bookStatus()));

        Publisher publisher = publisherRepository.findByName(request.publisher())
                .orElseGet(() -> {
                    Publisher newPublisher = new Publisher();
                    newPublisher.setName(request.publisher());
                    return publisherRepository.save(newPublisher);
                });
        book.setPublisher(publisher);

        Set<BookCategory> categoriesToRemove = new HashSet<>(book.getCategories());
        for (BookCategory bookCategory : categoriesToRemove) {
            book.getCategories().remove(bookCategory);
            bookCategory.getCategory().getBookCategories().remove(bookCategory);
            bookCategory.setBook(null);
            bookCategory.setCategory(null);
            entityManager.remove(bookCategory);
        }
        entityManager.flush();

        int count = 0;
        for (String categoryStr : request.categories()) {
            if (count >= MAX_CATEGORY_COUNT) {
                break;
            }
            Category leafCategory = processCategoryHierarchy(categoryStr);
            if (leafCategory != null) {
                BookCategory bookCategory = new BookCategory(book, leafCategory);
                book.addCategory(bookCategory);
                leafCategory.addBookCategory(bookCategory);
                count++;
            }
        }

        List<ParsedPerson> parsedPersons = parseAuthors(request.authors());

        Set<BookContributor> contributorsToRemove = new HashSet<>(book.getContributors());
        for (BookContributor bookContributor : contributorsToRemove) {
            book.getContributors().remove(bookContributor);
            bookContributor.getContributor().getBookContributors().remove(bookContributor);
            bookContributor.setBook(null);
            bookContributor.setContributor(null);
            entityManager.remove(bookContributor);
        }
        entityManager.flush();

        for (ParsedPerson person : parsedPersons) {
            Contributor contributor = contributorRepository.findByName(person.name)
                    .orElseGet(() -> {
                        Contributor newContributor = new Contributor();
                        newContributor.setName(person.name);
                        newContributor.setDescription("");
                        return contributorRepository.save(newContributor);
                    });

            ContributorRole contributorRole = switch (person.role) {
                case "지은이" -> ContributorRole.AUTHOR;
                case "옮긴이" -> ContributorRole.TRANSLATOR;
                default -> ContributorRole.EDITOR;
            };

            Role role = roleRepository.findByRoleName(contributorRole)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setRoleName(contributorRole);
                        return roleRepository.save(newRole);
                    });

            BookContributor bookContributor = new BookContributor();
            bookContributor.setBook(book);
            bookContributor.setContributor(contributor);
            bookContributor.setRole(role);
            book.getContributors().add(bookContributor);
        }

        Set<BookImage> imagesToRemove = new HashSet<>(book.getBookImages());
        for (BookImage bookImage : imagesToRemove) {
            book.removeBookImage(bookImage);
        }

        if (request.thumbnailImageUrl() != null && !request.thumbnailImageUrl().trim().isEmpty()) {
            Image image = new Image();
            image.setUrl(request.thumbnailImageUrl().trim());
            image.setType(ImageType.THUMBNAIL);
            image = imageRepository.save(image);
            entityManager.flush();

            BookImage bookImage = new BookImage();
            bookImage.setBook(book);

            book.addBookImage(bookImage);

            bookImageRepository.save(bookImage);
        }

        if (request.detailImageUrls() != null) {
            for (String imageUrl : request.detailImageUrls()) {
                if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                    Image image = new Image();
                    image.setUrl(imageUrl.trim());
                    image.setType(ImageType.DETAIL);
                    image = imageRepository.save(image);
                    entityManager.flush();

                    BookImage bookImage = new BookImage();
                    bookImage.setBook(book);

                    book.addBookImage(bookImage);

                    bookImageRepository.save(bookImage);
                }
            }
        }

        // 기존 태그 삭제
        Set<BookTag> tagsToRemove = new HashSet<>(book.getTags());
        for (BookTag bookTag : tagsToRemove) {
            book.getTags().remove(bookTag);
            bookTag.getTag().getBookTags().remove(bookTag);
            bookTag.setBook(null);
            bookTag.setTag(null);
            entityManager.remove(bookTag);
        }

        entityManager.flush();
        if (request.tags() != null) {
            for (String tagName : request.tags()) {
                if (tagName != null && !tagName.trim().isEmpty()) {
                    Tag tag = tagRepository.findByName(tagName.trim())
                            .orElseGet(() -> {
                                Tag newTag = new Tag();
                                newTag.setName(tagName.trim());
                                return tagRepository.save(newTag);
                            });
                    BookTag bookTag = new BookTag();
                    bookTag.setBook(book);
                    bookTag.setTag(tag);
                    book.getTags().add(bookTag);
                    tag.getBookTags().add(bookTag);
                }
            }
        }

        return new BookUpdateResponse(
                book.getTitle(),
                book.getIndex(),
                book.getDescription(),
                book.getPublication().toString(),
                book.getIsbn(),
                book.getBookImages().stream()
                        .filter(bookImage -> bookImage.getType() == ImageType.THUMBNAIL)
                        .map(Image::getUrl)
                        .findFirst()
                        .orElse(null),
                book.isPackable(),
                book.getStock(),
                book.getPrice().intValue(),
                book.getDiscountRate(),
                book.getStatus().toString(),
                book.getPublisher().getName(),
                book.getCategories().stream()
                        .map(bc -> bc.getCategory().getName())
                        .collect(Collectors.toList()),
                book.getContributors().stream()
                        .map(bc -> bc.getContributor().getName())
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public BookDeleteResponse deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        book.setStatus(BookStatus.DELETED);
        bookRepository.save(book);
        return new BookDeleteResponse("Book deleted successfully.");
    }

    @Transactional
    public BookDetailResponse getBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        if (book.getStatus() == BookStatus.DELETED) {
            throw new BookNotFoundException(bookId);
        }
        return new BookDetailResponse(
                book.getId(),
                book.getTitle(),
                book.getIndex(),
                book.getDescription(),
                book.getPublication().toString(),
                book.getIsbn(),
                book.getBookImages().stream()
                        .filter(bookImage -> bookImage.getType() == ImageType.THUMBNAIL)
                        .map(Image::getUrl)
                        .findFirst()
                        .orElse(null),
                book.getBookImages().stream()
                        .map(bookImage -> bookImage.getType().equals(ImageType.DETAIL)
                                ? bookImage.getUrl() : null).toList(),
                book.isPackable(),
                book.getStock(),
                book.getPrice().intValue(),
                book.getDiscountRate(),
                book.getStatus().toString(),
                book.getPublisher().getName(),
                book.getCategories().stream()
                        .map(bc -> buildCategoryPath(bc.getCategory()))
                        .collect(Collectors.toList()),
                book.getContributors().stream()
                        .filter(bc -> bc.getRole().getRoleName() == ContributorRole.AUTHOR)
                        .map(bc -> bc.getContributor().getName())
                        .collect(Collectors.toList()),
                book.getTags().stream()
                        .map(bt -> bt.getTag().getName())
                        .collect(Collectors.toList())
        );
    }

    private static class ParsedPerson {
        String name;
        String role;

        ParsedPerson(String name, String role) {
            this.name = name.trim();
            this.role = role.trim();
        }
    }

    private static class RolePosition {
        String role;
        int start;
        int end;

        RolePosition(String role, int start, int end) {
            this.role = role;
            this.start = start;
            this.end = end;
        }
    }

    private BookSearchResponse mapToBookSearchResponse(BookSearchApiResponse.Item item) {
        LocalDate publicationDate;
        try {
            publicationDate = LocalDate.parse(item.getPubDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            publicationDate = LocalDate.now();
        }

        return new BookSearchResponse(
                item.getTitle(),
                item.getCover(),
                item.getDescription(),
                item.getCategory(),
                item.getAuthor(),
                item.getPublisher(),
                publicationDate,
                item.getIsbn(),
                item.getStandardPrice(),
                item.getSalesPrice()
        );
    }

    private Category processCategoryHierarchy(String categoriesStr) {
        if (categoriesStr == null || categoriesStr.trim().isEmpty()) {
            return null;
        }

        String[] categoryNames = categoriesStr.split(">");
        Category parentCategory = null;
        Category currentCategory = null;

        for (String categoryName : categoryNames) {
            String trimmedName = categoryName.trim();
            if (trimmedName.isEmpty()) {
                continue;
            }

            Optional<Category> categoryOpt = categoryRepository.findByNameAndParentCategory(trimmedName, parentCategory);

            Category finalParentCategory = parentCategory;
            currentCategory = categoryOpt.orElseGet(() -> {
                Category newCategory = new Category();
                newCategory.setName(trimmedName);
                newCategory.setParentCategory(finalParentCategory);
                return categoryRepository.save(newCategory);
            });

            parentCategory = currentCategory;
        }

        return currentCategory;
    }

    private List<ParsedPerson> parseAuthors(String authorsStr) {
        List<ParsedPerson> persons = new ArrayList<>();

        Pattern rolePattern = Pattern.compile("\\([^\\)]*\\)");
        Matcher roleMatcher = rolePattern.matcher(authorsStr);

        List<RolePosition> roles = new ArrayList<>();

        while (roleMatcher.find()) {
            String roleText = roleMatcher.group();
            int start = roleMatcher.start();
            int end = roleMatcher.end();
            roles.add(new RolePosition(roleText.substring(1, roleText.length() - 1), start, end));
        }

        int lastEnd = 0;
        for (RolePosition role : roles) {
            int start = role.start;
            String segment = authorsStr.substring(lastEnd, start);
            String[] names = segment.split(",");
            for (String name : names) {
                name = name.trim();
                if (!name.isEmpty()) {
                    persons.add(new ParsedPerson(name, role.role));
                }
            }
            lastEnd = role.end;
        }

        if (lastEnd < authorsStr.length()) {
            String segment = authorsStr.substring(lastEnd);
            String[] names = segment.split(",");
            for (String name : names) {
                name = name.trim();
                if (!name.isEmpty()) {
                    persons.add(new ParsedPerson(name, "기타"));
                }
            }
        }

        return persons;
    }

    private String buildCategoryPath(Category category) {
        List<String> categoryNames = new LinkedList<>();
        while (category != null) {
            categoryNames.addFirst(category.getName());
            category = category.getParentCategory();
        }
        return String.join(">", categoryNames);
    }

    public List<BookCartResponse> getBooksSummary(List<Long> bookIds) {
        List<Book> books = bookRepository.findAllByIdInAndType(bookIds, ImageType.THUMBNAIL);
        return books.stream()
                .map(book -> new BookCartResponse(
                                book.getId(),
                                book.getTitle(),
                                bookFormatter.formatPrice(book.getPrice()),
                                bookFormatter.formatPrice(getSalePrice(book.getPrice(), book.getDiscountRate())),
                                book.getDiscountRate(),
                                book.getBookImages().stream()
                                        .map(Image::getUrl)
                                        .findFirst()
                                        .orElse("no-image")
                        )
                )
                .collect(Collectors.toList());
    }

    public Page<BookResponse> getBooks(Pageable pageable) {
        Page<Book> allByPageable = bookRepository.findAllByPageable(pageable);
        return allByPageable.map(book -> {
            return new BookResponse(
                    book.getId(),
                    book.getTitle(),
                    bookFormatter.formatDate(book.getPublication()),
                    bookFormatter.formatPrice(getSalePrice(book.getPrice(), book.getDiscountRate())),
                    book.getDiscountRate(),
                    book.getBookImages().stream()
                            .filter(bookImage -> bookImage.getType() == ImageType.THUMBNAIL)
                            .map(Image::getUrl)
                            .findFirst()
                            .orElse("no-image"),
                    book.getContributors().stream()
                            .map(bookContributor -> new ContributorSummaryResponse(
                                    bookContributor.getContributor().getId(),
                                    bookContributor.getContributor().getName()
                            ))
                            .collect(Collectors.toList()),
                    new PublisherSummaryResponse(
                            book.getPublisher().getId(),
                            book.getPublisher().getName()
                    )
            );
        });
    }

    private BigDecimal getSalePrice(BigDecimal price, double disCountRate) {
        BigDecimal discountRate = BigDecimal.valueOf(disCountRate).divide(BigDecimal.valueOf(100));
        return price.multiply(BigDecimal.ONE.subtract(discountRate)).setScale(2, RoundingMode.HALF_UP);
    }
}
