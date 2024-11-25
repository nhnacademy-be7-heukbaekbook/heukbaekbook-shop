package com.nhnacademy.heukbaekbookshop.book.dto.request.book;

import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;

import java.util.List;

public record BookSearchCondition(List<Long> bookIds,
                                  ImageType imageType) {
}
