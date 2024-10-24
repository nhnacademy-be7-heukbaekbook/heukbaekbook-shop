package com.nhnacademy.heukbaekbookshop.book.service;

import com.nhnacademy.heukbaekbookshop.book.dto.BookSearchApiResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.BookSearchRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.BookSearchResponse;
import com.nhnacademy.heukbaekbookshop.book.exception.BookSearchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final RestTemplate restTemplate;

    @Value("${aladin.api.key}")
    private String aladinApiKey;

    public BookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
            throw new BookSearchException("Aladin API 호출 중 오류가 발생했습니다.", e);
        }
    }

    // 쿼리 파라미터를 URL 인코딩하는 메서드
    // private String encodeQuery(String query) {
        // return URLEncoder.encode(query, StandardCharsets.UTF_8);
    // }

    // Item 객체를 BookSearchResponse로 변환하는 메서드
    private BookSearchResponse mapToBookSearchResponse(BookSearchApiResponse.Item item) {
        LocalDate publicationDate = null;
        try {
            publicationDate = LocalDate.parse(item.getPubDate(), DateTimeFormatter.ISO_DATE);
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
}
