package com.nhnacademy.heukbaekbookshop.category.dto.response;

public record CategoryDetailResponse (
        Long id,
        Long parentId,
        String name
) {}
