package com.nhnacademy.heukbaekbookshop.order.controller;

import com.nhnacademy.heukbaekbookshop.order.dto.response.WrappingPaperResponse;
import com.nhnacademy.heukbaekbookshop.order.service.WrappingPaperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/wrapping-papers")
public class WrappingPaperController {

    private final WrappingPaperService wrappingPaperService;

    @GetMapping
    public List<WrappingPaperResponse> getAllWrappingPapers() {
        return wrappingPaperService.getAllWrappingPapers();
    }
}
