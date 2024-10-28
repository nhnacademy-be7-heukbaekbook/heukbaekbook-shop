package com.nhnacademy.heukbaekbookshop.book.service.book;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.domain.BookCategory;
import com.nhnacademy.heukbaekbookshop.book.domain.BookStatus;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookCreateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchRequest;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final ContributorRepository contributorRepository;
    private final RoleRepository roleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${aladin.api.key}")
    private String aladinApiKey;

    public BookService(
            RestTemplate restTemplate,
            BookRepository bookRepository,
            PublisherRepository publisherRepository,
            CategoryRepository categoryRepository,
            ContributorRepository contributorRepository,
            RoleRepository roleRepository
    ) {
        this.restTemplate = restTemplate;
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.categoryRepository = categoryRepository;
        this.contributorRepository = contributorRepository;
        this.roleRepository = roleRepository;
    }

    public List<BookSearchResponse> searchBook(BookSearchRequest bookSearchRequest) {
        String url = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx" +
                "?ttbkey=" + aladinApiKey +
                "&Query=" + bookSearchRequest.title() +
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

        for (String categoryName : request.categories()) {
            Category category = categoryRepository.findByName(categoryName.trim())
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(categoryName.trim());
                        return categoryRepository.save(newCategory);
                    });

            BookCategory bookCategory = new BookCategory(book, category);

            book.addCategory(bookCategory);
            category.addBookCategory(bookCategory);
        }

        for (String authorName : request.authors()) {
            Contributor contributor = contributorRepository.findByName(authorName.trim())
                    .orElseGet(() -> {
                        Contributor newContributor = new Contributor();
                        newContributor.setName(authorName.trim());
                        newContributor.setDescription("");
                        return contributorRepository.save(newContributor);
                    });

            Role role = roleRepository.findByRoleName(ContributorRole.AUTHOR)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setRoleName(ContributorRole.AUTHOR);
                        return roleRepository.save(newRole);
                    });

            BookContributor bookContributor = new BookContributor();
            bookContributor.setBook(book);
            bookContributor.setContributor(contributor);
            bookContributor.setRole(role);

            book.getContributors().add(bookContributor);
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
                        .filter(bc -> bc.getRole().getRoleName() == ContributorRole.AUTHOR)
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

        Set<Category> newCategories = new HashSet<>();
        for (String categoryName : request.categories()) {
            Category category = categoryRepository.findByName(categoryName.trim())
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(categoryName.trim());
                        return categoryRepository.save(newCategory);
                    });
            newCategories.add(category);
        }

        Set<BookCategory> categoriesToRemove = new HashSet<>(book.getCategories());
        for (BookCategory bookCategory : categoriesToRemove) {

            book.getCategories().remove(bookCategory);

            bookCategory.getCategory().getBookCategories().remove(bookCategory);

            bookCategory.setBook(null);
            bookCategory.setCategory(null);

            entityManager.remove(bookCategory);
        }

        entityManager.flush();

        for (Category category : newCategories) {
            if (book.getId() == null || category.getId() == null) {
                throw new IllegalStateException("Book ID or Category ID is null.");
            }
            BookCategory bookCategory = new BookCategory(book, category);
            book.addCategory(bookCategory);
            category.addBookCategory(bookCategory);
        }

        Set<Contributor> newContributors = new HashSet<>();
        for (String authorName : request.authors()) {
            Contributor contributor = contributorRepository.findByName(authorName.trim())
                    .orElseGet(() -> {
                        Contributor newContributor = new Contributor();
                        newContributor.setName(authorName.trim());
                        newContributor.setDescription("");
                        return contributorRepository.save(newContributor);
                    });
            newContributors.add(contributor);
        }

        Set<BookContributor> contributorsToRemove = new HashSet<>(book.getContributors());
        for (BookContributor bookContributor : contributorsToRemove) {
            book.getContributors().remove(bookContributor);

            bookContributor.getContributor().getBookContributors().remove(bookContributor);

            bookContributor.setBook(null);
            bookContributor.setContributor(null);
        }
        Role authorRole = roleRepository.findByRoleName(ContributorRole.AUTHOR)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName(ContributorRole.AUTHOR);
                    return roleRepository.save(newRole);
                });

        for (Contributor contributor : newContributors) {
            BookContributor bookContributor = new BookContributor();
            bookContributor.setBook(book);
            bookContributor.setContributor(contributor);
            bookContributor.setRole(authorRole);
            book.getContributors().add(bookContributor);
        }

        bookRepository.save(book);
        return new BookUpdateResponse(
                book.getTitle(),
                book.getIndex(),
                book.getDescription(),
                book.getPublication().toString(),
                book.getIsbn(),
                book.isPackable(),
                book.getStock(),
                book.getPrice().intValue(),
                book.getDiscountRate(),
                book.getStatus().name(),
                book.getPublisher().getName(),
                book.getCategories().stream()
                        .map(bc -> bc.getCategory().getName())
                        .collect(Collectors.toList()),
                book.getContributors().stream()
                        .filter(bc -> bc.getRole().getRoleName() == ContributorRole.AUTHOR)
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
                        .filter(bc -> bc.getRole().getRoleName() == ContributorRole.AUTHOR)
                        .map(bc -> bc.getContributor().getName())
                        .collect(Collectors.toList())

        );
    }
}
