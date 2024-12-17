package com.nhnacademy.heukbaekbookshop.contributor.controller;

import com.nhnacademy.heukbaekbookshop.contributor.dto.request.ContributorCreateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.ContributorUpdateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorCreateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorDeleteResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorDetailResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorUpdateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.service.ContributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/contributors")
public class ContributorController {

    private final ContributorService contributorService;

    @Autowired
    public ContributorController(ContributorService contributorService) {
        this.contributorService = contributorService;
    }

    @PostMapping
    public ResponseEntity<ContributorCreateResponse> registerContributor(@RequestBody ContributorCreateRequest request) {
        ContributorCreateResponse response = contributorService.registerContributor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ContributorDetailResponse>> getContributors(Pageable pageable) {
        Page<ContributorDetailResponse> responses = contributorService.getContributors(pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{contributor-id}")
    public ResponseEntity<ContributorDetailResponse> getContributor(@PathVariable(name = "contributor-id") Long contributorId) {
        ContributorDetailResponse response = contributorService.getContributor(contributorId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PutMapping("/{contributor-id}")
    public ResponseEntity<ContributorUpdateResponse> updateContributor(
            @PathVariable(name = "contributor-id") Long contributorId,
            @RequestBody ContributorUpdateRequest request) {
        ContributorUpdateResponse response = contributorService.updateContributor(contributorId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{contributor-id}")
    public ResponseEntity<ContributorDeleteResponse> deleteContributor(@PathVariable(name = "contributor-id") Long contributorId) {
        ContributorDeleteResponse response = contributorService.deleteContributor(contributorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
