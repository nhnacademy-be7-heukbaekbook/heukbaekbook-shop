package com.nhnacademy.heukbaekbookshop.tag.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.tag.dto.request.TagCreateRequest;
import com.nhnacademy.heukbaekbookshop.tag.dto.request.TagUpdateRequest;
import com.nhnacademy.heukbaekbookshop.tag.dto.response.TagCreateResponse;
import com.nhnacademy.heukbaekbookshop.tag.dto.response.TagDeleteResponse;
import com.nhnacademy.heukbaekbookshop.tag.dto.response.TagDetailResponse;
import com.nhnacademy.heukbaekbookshop.tag.dto.response.TagUpdateResponse;
import com.nhnacademy.heukbaekbookshop.tag.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TagService tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterTag() throws Exception {
        TagCreateRequest request = new TagCreateRequest("Technology");
        TagCreateResponse response = new TagCreateResponse("Technology");

        when(tagService.registerTag(any(TagCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void testUpdateTag() throws Exception {
        // given
        TagUpdateRequest request = new TagUpdateRequest("tag");
        TagUpdateResponse response = new TagUpdateResponse("tag");

        when(tagService.updateTag(any(Long.class), any(TagUpdateRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(put("/api/admin/tags/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void testDeleteTag() throws Exception {
        // given
        Long tagId = 1L;
        TagDeleteResponse response = new TagDeleteResponse(tagId, "tag");

        when(tagService.deleteTag(tagId)).thenReturn(response);

        // when & then
        mockMvc.perform(delete("/api/admin/tags/{tagId}", tagId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void testGetTag() throws Exception {
        // Given
        Long tagId = 1L;
        TagDetailResponse response = new TagDetailResponse(tagId, "Sample Tag");

        when(tagService.getTag(tagId)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/admin/tags/{tagId}", tagId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tagId))
                .andExpect(jsonPath("$.name").value("Sample Tag"));
    }

    @Test
    void testGetTags() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        TagDetailResponse tag1 = new TagDetailResponse(1L, "Tag1");
        TagDetailResponse tag2 = new TagDetailResponse(2L, "Tag2");
        Page<TagDetailResponse> response = new PageImpl<>(List.of(tag1, tag2), pageable, 2);

        when(tagService.getTags(pageable)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/admin/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Tag1"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].name").value("Tag2"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void testGetTagList() throws Exception {
        // Given
        List<String> tagList = List.of("Tag1", "Tag2", "Tag3");
        when(tagService.getTagList()).thenReturn(tagList);

        // When & Then
        mockMvc.perform(get("/api/admin/tags/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Tag1"))
                .andExpect(jsonPath("$[1]").value("Tag2"))
                .andExpect(jsonPath("$[2]").value("Tag3"));
    }

}
