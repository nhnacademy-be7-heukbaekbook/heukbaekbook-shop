package com.nhnacademy.heukbaekbookshop.tag;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.tag.controller.TagController;
import com.nhnacademy.heukbaekbookshop.tag.dto.request.TagCreateRequest;
import com.nhnacademy.heukbaekbookshop.tag.dto.request.TagUpdateRequest;
import com.nhnacademy.heukbaekbookshop.tag.dto.response.*;
import com.nhnacademy.heukbaekbookshop.tag.exception.TagAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.tag.exception.TagNotFoundException;
import com.nhnacademy.heukbaekbookshop.tag.service.TagService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Autowired
    private ObjectMapper objectMapper;

    // 성공 케이스 테스트
    @Test
    void registerTag_Success() throws Exception {
        // Given
        TagCreateRequest request = new TagCreateRequest("newTag");
        TagCreateResponse response = new TagCreateResponse("newTag");

        Mockito.when(tagService.registerTag(any(TagCreateRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    TagCreateResponse actualResponse = objectMapper.readValue(responseBody, TagCreateResponse.class);
                    assertEquals("newTag", actualResponse.name());
                });
    }

    // 오류 발생이 정상인 케이스 테스트 (예외 발생 확인)
    @Test
    void registerTag_AlreadyExists() throws Exception {
        // Given
        TagCreateRequest request = new TagCreateRequest("existingTag");

        Mockito.when(tagService.registerTag(any(TagCreateRequest.class)))
                .thenThrow(new TagAlreadyExistsException("이미 존재하는 태그입니다."));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(post("/api/tags")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andReturn();
        });

        // 예외가 발생하는 것이 정상적인 응답임을 확인
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof TagAlreadyExistsException);
        assertEquals("이미 존재하는 태그입니다.", cause.getMessage());
    }

    // 성공 케이스 테스트
    @Test
    void updateTag_Success() throws Exception {
        // Given
        Long tagId = 1L;
        TagUpdateRequest request = new TagUpdateRequest("updatedTag");
        TagUpdateResponse response = new TagUpdateResponse("updatedTag");

        Mockito.when(tagService.updateTag(eq(tagId), any(TagUpdateRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(put("/api/tags/{tagId}", tagId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    TagUpdateResponse actualResponse = objectMapper.readValue(responseBody, TagUpdateResponse.class);
                    assertEquals("updatedTag", actualResponse.name());
                });
    }

    // 오류 발생이 정상인 케이스 테스트 (예외 발생 확인)
    @Test
    void updateTag_NotFound() throws Exception {
        // Given
        Long tagId = 1L;
        TagUpdateRequest request = new TagUpdateRequest("updatedTag");

        Mockito.when(tagService.updateTag(eq(tagId), any(TagUpdateRequest.class)))
                .thenThrow(new TagNotFoundException("존재하지 않는 태그입니다."));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(put("/api/tags/{tagId}", tagId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andReturn();
        });

        // 예외가 발생하는 것이 정상적인 응답임을 확인
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof TagNotFoundException);
        assertEquals("존재하지 않는 태그입니다.", cause.getMessage());
    }

    // 성공 케이스 테스트
    @Test
    void deleteTag_Success() throws Exception {
        // Given
        Long tagId = 1L;
        TagDeleteResponse response = new TagDeleteResponse(tagId, "deletedTag");

        Mockito.when(tagService.deleteTag(tagId)).thenReturn(response);

        // When & Then
        mockMvc.perform(delete("/api/tags/{tagId}", tagId))
                .andExpect(status().isNoContent());
    }

    // 오류 발생이 정상인 케이스 테스트 (예외 발생 확인)
    @Test
    void deleteTag_NotFound() throws Exception {
        // Given
        Long tagId = 1L;

        Mockito.when(tagService.deleteTag(tagId))
                .thenThrow(new TagNotFoundException("존재하지 않는 태그입니다."));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(delete("/api/tags/{tagId}", tagId))
                    .andReturn();
        });

        // 예외가 발생하는 것이 정상적인 응답임을 확인
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof TagNotFoundException);
        assertEquals("존재하지 않는 태그입니다.", cause.getMessage());
    }

    // 성공 케이스 테스트
    @Test
    void getTag_Success() throws Exception {
        // Given
        Long tagId = 1L;
        TagDetailResponse response = new TagDetailResponse("existingTag");

        Mockito.when(tagService.getTag(tagId)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/tags/{tagId}", tagId))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    TagDetailResponse actualResponse = objectMapper.readValue(responseBody, TagDetailResponse.class);
                    assertEquals("existingTag", actualResponse.name());
                });
    }

    // 오류 발생이 정상인 케이스 테스트 (예외 발생 확인)
    @Test
    void getTag_NotFound() throws Exception {
        // Given
        Long tagId = 1L;

        Mockito.when(tagService.getTag(tagId))
                .thenThrow(new TagNotFoundException("존재하지 않는 태그입니다."));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(get("/api/tags/{tagId}", tagId))
                    .andReturn();
        });

        // 예외가 발생하는 것이 정상적인 응답임을 확인
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof TagNotFoundException);
        assertEquals("존재하지 않는 태그입니다.", cause.getMessage());
    }
}
