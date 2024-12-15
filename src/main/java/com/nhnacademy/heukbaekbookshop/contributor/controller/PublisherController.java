package com.nhnacademy.heukbaekbookshop.contributor.controller;

import com.nhnacademy.heukbaekbookshop.contributor.dto.request.PublisherCreateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.PublisherUpdateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherCreateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherDeleteResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherDetailResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherUpdateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    public ResponseEntity<PublisherCreateResponse> registerPublisher(@RequestBody PublisherCreateRequest request) {
        PublisherCreateResponse response = publisherService.registerPublisher(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<PublisherDetailResponse>> getPublishers(Pageable pageable) {
        Page<PublisherDetailResponse> response = publisherService.getPublishers(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{publisher-id}")
    public ResponseEntity<PublisherDetailResponse> getPublisherById(@PathVariable(name = "publisher-id") Long publisherId) {
        PublisherDetailResponse response = publisherService.getPublisherById(publisherId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{publisher-id}")
    public ResponseEntity<PublisherUpdateResponse> updatePublisher(
            @PathVariable(name = "publisher-id") Long publisherId,
            @RequestBody PublisherUpdateRequest request) {
        PublisherUpdateResponse response = publisherService.updatePublisher(publisherId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{publisher-id}")
    public ResponseEntity<PublisherDeleteResponse> deletePublisher(@PathVariable(name = "publisher-id") Long publisherId) {
        PublisherDeleteResponse response = publisherService.deletePublisher(publisherId);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

}
