package com.nhnacademy.heukbaekbookshop.book.service.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import com.nhnacademy.heukbaekbookshop.book.domain.BookStatus;
import com.nhnacademy.heukbaekbookshop.book.domain.BookTag;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookCreateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchCondition;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookUpdateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.*;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookSearchException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
import com.nhnacademy.heukbaekbookshop.contributor.domain.*;
import com.nhnacademy.heukbaekbookshop.contributor.repository.ContributorRepository;
import com.nhnacademy.heukbaekbookshop.contributor.repository.PublisherRepository;
import com.nhnacademy.heukbaekbookshop.contributor.repository.RoleRepository;
import com.nhnacademy.heukbaekbookshop.image.domain.BookImage;
import com.nhnacademy.heukbaekbookshop.image.domain.Image;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.nhnacademy.heukbaekbookshop.image.repository.BookImageRepository;
import com.nhnacademy.heukbaekbookshop.tag.domain.Tag;
import com.nhnacademy.heukbaekbookshop.tag.repository.TagRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookService {

    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final ContributorRepository contributorRepository;
    private final RoleRepository roleRepository;
    private final BookImageRepository bookImageRepository;
    private final TagRepository tagRepository;

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
        book.setPublishedAt(Date.valueOf(String.valueOf(request.publishedAt())));
        book.setIsbn(request.isbn());
        book.setPrice(BigDecimal.valueOf(request.standardPrice()));
        book.setDiscountRate(request.discountRate());
        book.setPackable(request.isPackable());
        book.setPublisher(publisher);
        book.setStatus(BookStatus.IN_STOCK);
        book.setStock(request.stock());
        book.setCategories(new ArrayList<>());
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

            BookImage bookImage = new BookImage();
            bookImage.setBook(book);
            bookImage.setUrl(request.imageUrl().trim());
            bookImage.setType(ImageType.THUMBNAIL);

            book.addBookImage(bookImage);

            bookImageRepository.save(bookImage);
        }

        bookRepository.save(book);

        return new BookCreateResponse(
                book.getTitle(),
                book.getIndex(),
                book.getDescription(),
                book.getPublishedAt().toString(),
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
        // 1 수정
        book.setTitle(request.title());
        book.setIndex(request.index());
        book.setDescription(request.description());
        book.setPublishedAt(Date.valueOf(request.publishedAt()));
        book.setIsbn(request.isbn());
        book.setPackable(request.isPackable());
        book.setStock(request.stock());
        book.setPrice(BigDecimal.valueOf(request.standardPrice()));
        book.setDiscountRate(request.discountRate());
        book.setStatus(BookStatus.valueOf(request.bookStatus()));

        // 2 출판사 수정
        Publisher publisher = publisherRepository.findByName(request.publisher())
                .orElseGet(() -> {
                    Publisher newPublisher = new Publisher();
                    newPublisher.setName(request.publisher());
                    return publisherRepository.save(newPublisher);
                });
        book.setPublisher(publisher);

        // 3 카테고리 수정
        Set<BookCategory> categoriesToRemove = new HashSet<>(book.getCategories());
        for (BookCategory bookCategory : categoriesToRemove) {
            book.getCategories().remove(bookCategory);
            bookCategory.getCategory().getBookCategories().remove(bookCategory);
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
        // 4 기여자(저자, 옮긴이, 엮은이) 수정
        Set<BookContributor> contributorsToRemove = new HashSet<>(book.getContributors());
        for (BookContributor bookContributor : contributorsToRemove) {
            book.getContributors().remove(bookContributor);
            bookContributor.getContributor().getBookContributors().remove(bookContributor);
            bookContributor.setBook(null);
            bookContributor.setContributor(null);
            entityManager.remove(bookContributor);
        }
        entityManager.flush();

        // 5 기여자 처리
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
            BookImage bookImage = new BookImage();
            bookImage.setBook(book);
            bookImage.setUrl(request.thumbnailImageUrl().trim());
            bookImage.setType(ImageType.THUMBNAIL);

            book.addBookImage(bookImage);

            bookImageRepository.save(bookImage);
        }

        if (request.detailImageUrls() != null) {
            for (String imageUrl : request.detailImageUrls()) {
                if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                    BookImage bookImage = new BookImage();
                    bookImage.setBook(book);
                    bookImage.setUrl(imageUrl.trim());
                    bookImage.setType(ImageType.DETAIL);
                    book.addBookImage(bookImage);
                    bookImageRepository.save(bookImage);
                }
            }
        }

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
            book.getTags().clear();
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
                    book.addTag(bookTag);
                    tag.getBookTags().add(bookTag);
                }
            }
        }

        bookRepository.save(book);

        return new BookUpdateResponse(
                book.getTitle(),
                book.getIndex(),
                book.getDescription(),
                book.getPublishedAt().toString(),
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
                book.getPublishedAt().toString(),
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

    @Transactional
    public void increasePopularity(Long bookId) {
        bookRepository.increasePopularityByBookId(bookId);
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

    public List<BookSummaryResponse> getBooksSummary(List<Long> bookIds) {
        List<Book> books = bookRepository.findAllByBookSearchCondition(new BookSearchCondition(bookIds, ImageType.THUMBNAIL));
        return books.stream()
                .map(BookSummaryResponse::of)
                .collect(Collectors.toList());
    }

    public Page<BookResponse> getBooks(Pageable pageable) {
        Page<Book> allByPageable = bookRepository.findAllByPageable(pageable);
        return allByPageable.map(BookResponse::of);
    }

    public Page<BookResponse> getBooksByCategoryId(Long categoryId, Pageable pageable) {
        List<Long> categoryIds = categoryRepository.findSubCategoryIdsByCategoryId(categoryId);
        Page<Book> books = bookRepository.findAllByCategoryIds(categoryIds, pageable);

        return books.map(BookResponse::of);
    }

    public BookViewResponse getBookDetail(Long bookId) {
        Book book = bookRepository.findByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        return BookViewResponse.of(book);
    }

    public Page<BookDetailResponse> getBooksDetail(Pageable pageable) {
        Page<Book> booksPage = bookRepository.findAllByStatusNot(BookStatus.DELETED, pageable);

        return booksPage.map(book -> new BookDetailResponse(
                book.getId(),
                book.getTitle(),
                book.getIndex(),
                book.getDescription(),
                book.getPublishedAt().toString(),
                book.getIsbn(),
                book.getBookImages().stream()
                        .filter(bookImage -> bookImage.getType() == ImageType.THUMBNAIL)
                        .map(Image::getUrl)
                        .findFirst()
                        .orElse(null),
                book.getBookImages().stream()
                        .filter(bookImage -> bookImage.getType() == ImageType.DETAIL)
                        .map(Image::getUrl)
                        .collect(Collectors.toList()),
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
        ));
    }
}
