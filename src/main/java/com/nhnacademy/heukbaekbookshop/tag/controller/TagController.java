package com.nhnacademy.heukbaekbookshop.tag.controller;

import com.nhnacademy.heukbaekbookshop.tag.dto.request.TagCreateRequest;
import com.nhnacademy.heukbaekbookshop.tag.dto.request.TagUpdateRequest;
import com.nhnacademy.heukbaekbookshop.tag.dto.response.TagCreateResponse;
import com.nhnacademy.heukbaekbookshop.tag.dto.response.TagDeleteResponse;
import com.nhnacademy.heukbaekbookshop.tag.dto.response.TagDetailResponse;
import com.nhnacademy.heukbaekbookshop.tag.dto.response.TagUpdateResponse;
import com.nhnacademy.heukbaekbookshop.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<TagCreateResponse> registerTag(@RequestBody TagCreateRequest request) {
        TagCreateResponse response = tagService.registerTag(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<TagUpdateResponse> updateTag(@PathVariable Long tagId, @RequestBody TagUpdateRequest request) {
        TagUpdateResponse response = tagService.updateTag(tagId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<TagDeleteResponse> deleteTag(@PathVariable Long tagId) {
        TagDeleteResponse response = tagService.deleteTag(tagId);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<TagDetailResponse> getTag(@PathVariable Long tagId) {
        TagDetailResponse response = tagService.getTag(tagId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<TagDetailResponse>> getTags(Pageable pageable) {
        Page<TagDetailResponse> response = tagService.getTags(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public List<String> getTagList() {
        return tagService.getTagList();
    }
}
