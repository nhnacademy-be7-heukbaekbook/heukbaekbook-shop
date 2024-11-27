package com.nhnacademy.heukbaekbookshop.order.repository;

import com.nhnacademy.heukbaekbookshop.order.domain.WrappingPaper;

import java.util.List;

public interface WrappingPaperRepositoryCustom {

    List<WrappingPaper> searchAll();
}
