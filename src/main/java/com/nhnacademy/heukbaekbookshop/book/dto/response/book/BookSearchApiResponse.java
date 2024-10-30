package com.nhnacademy.heukbaekbookshop.book.dto.response.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookSearchApiResponse {

    @JsonProperty("item")
    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private String title;
        @JsonProperty("cover")
        private String cover;
        private String description;

        @JsonProperty("categoryName")
        private String category;

        private String author;
        private String publisher;

        @JsonProperty("pubDate")
        private String pubDate;

        @JsonProperty("isbn13")
        private String isbn;

        @JsonProperty("priceStandard")
        private int standardPrice;

        @JsonProperty("priceSales")
        private int salesPrice;
    }
}