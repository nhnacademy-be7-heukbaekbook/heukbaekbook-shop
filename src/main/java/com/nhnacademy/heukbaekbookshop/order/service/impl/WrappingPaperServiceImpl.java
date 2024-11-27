package com.nhnacademy.heukbaekbookshop.order.service.impl;

import com.nhnacademy.heukbaekbookshop.order.domain.WrappingPaper;
import com.nhnacademy.heukbaekbookshop.order.dto.response.WrappingPaperResponse;
import com.nhnacademy.heukbaekbookshop.order.repository.WrappingPaperRepository;
import com.nhnacademy.heukbaekbookshop.order.service.WrappingPaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WrappingPaperServiceImpl implements WrappingPaperService {

    private final WrappingPaperRepository wrappingPaperRepository;

    @Override
    public List<WrappingPaperResponse> getAllWrappingPapers() {
        List<WrappingPaper> wrappingPapers = wrappingPaperRepository.searchAll();
        return wrappingPapers.stream()
                .map(WrappingPaperResponse::of)
                .collect(Collectors.toList());
    }
}
