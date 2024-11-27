package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.order.dto.response.WrappingPaperResponse;

import java.util.List;

public interface WrappingPaperService {
    List<WrappingPaperResponse> getAllWrappingPapers();
}
