package com.nhnacademy.heukbaekbookshop.contributor.controller;

import com.nhnacademy.heukbaekbookshop.contributor.dto.request.PublisherCreateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.PublisherUpdateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherCreateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherDeleteResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherDetailResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherUpdateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publishers")
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

    @GetMapping("/{publisherId}")
    public ResponseEntity<PublisherDetailResponse> getPublisher(@PathVariable Long publisherId) {
        PublisherDetailResponse response = publisherService.getPublisher(publisherId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{publisherId}")
    public ResponseEntity<PublisherUpdateResponse> updatePublisher(
            @PathVariable Long publisherId,
            @RequestBody PublisherUpdateRequest request) {
        PublisherUpdateResponse response = publisherService.updatePublisher(publisherId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{publisherId}")
    public ResponseEntity<PublisherDeleteResponse> deletePublisher(@PathVariable Long publisherId) {
        PublisherDeleteResponse response = publisherService.deletePublisher(publisherId);
        return ResponseEntity.ok(response);
    }

}
