package com.nhnacademy.heukbaekbookshop.order.repository;

import com.nhnacademy.heukbaekbookshop.order.domain.WrappingPaper;

import java.util.List;
import java.util.Optional;

public interface WrappingPaperRepositoryCustom {

    Optional<WrappingPaper> searchById(Long id);

    List<WrappingPaper> searchAll();
}
