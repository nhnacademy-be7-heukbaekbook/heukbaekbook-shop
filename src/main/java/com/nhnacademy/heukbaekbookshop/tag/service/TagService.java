package com.nhnacademy.heukbaekbookshop.tag.service;

import com.nhnacademy.heukbaekbookshop.tag.domain.Tag;
import com.nhnacademy.heukbaekbookshop.tag.dto.request.TagCreateRequest;
import com.nhnacademy.heukbaekbookshop.tag.dto.request.TagUpdateRequest;
import com.nhnacademy.heukbaekbookshop.tag.dto.response.TagCreateResponse;
import com.nhnacademy.heukbaekbookshop.tag.dto.response.TagDeleteResponse;
import com.nhnacademy.heukbaekbookshop.tag.dto.response.TagDetailResponse;
import com.nhnacademy.heukbaekbookshop.tag.dto.response.TagUpdateResponse;
import com.nhnacademy.heukbaekbookshop.tag.exception.TagAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.tag.exception.TagNotFoundException;
import com.nhnacademy.heukbaekbookshop.tag.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagCreateResponse registerTag(TagCreateRequest request) {
        if (tagRepository.findByName(request.name()).isPresent()) {
            throw new TagAlreadyExistsException("이미 존재하는 태그입니다.");
        }
        Tag tag = new Tag();
        tag.setName(request.name());
        tagRepository.save(tag);
        return new TagCreateResponse(request.name());
    }

    public TagUpdateResponse updateTag(Long tagId, TagUpdateRequest request) {
        if (tagRepository.findById(tagId).isEmpty()) {
            throw new TagNotFoundException("존재하지 않는 태그입니다.");
        }
        Tag tag = tagRepository.findById(tagId).get();
        tag.setName(request.name());
        tagRepository.save(tag);

        return new TagUpdateResponse(request.name());
    }

    public TagDeleteResponse deleteTag(Long tagId) {
        if (tagRepository.findById(tagId).isEmpty()) {
            throw new TagNotFoundException("존재하지 않는 태그입니다.");
        }
        Tag tag = tagRepository.findById(tagId).get();
        tagRepository.delete(tag);
        return new TagDeleteResponse(tagId, tag.getName());
    }

    public TagDetailResponse getTag(Long tagId) {
        if (tagRepository.findById(tagId).isEmpty()) {
            throw new TagNotFoundException("존재하지 않는 태그입니다.");
        }
        Tag tag = tagRepository.findById(tagId).get();
        return new TagDetailResponse(tag.getId(), tag.getName());
    }

    public Page<TagDetailResponse> getTags(Pageable pageable) {
        Page<Tag> tags = tagRepository.findAll(pageable);
        return tags.map(tag -> new TagDetailResponse(tag.getId(), tag.getName()));
    }

    public List<String> getTagList() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(Tag::getName).toList();
    }
}
