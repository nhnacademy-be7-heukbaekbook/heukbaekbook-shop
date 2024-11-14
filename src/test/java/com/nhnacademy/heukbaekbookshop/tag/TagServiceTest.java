//package com.nhnacademy.heukbaekbookshop.tag;
//
//import com.nhnacademy.heukbaekbookshop.tag.domain.Tag;
//import com.nhnacademy.heukbaekbookshop.tag.dto.request.TagCreateRequest;
//import com.nhnacademy.heukbaekbookshop.tag.dto.request.TagUpdateRequest;
//import com.nhnacademy.heukbaekbookshop.tag.dto.response.*;
//import com.nhnacademy.heukbaekbookshop.tag.exception.*;
//import com.nhnacademy.heukbaekbookshop.tag.repository.TagRepository;
//import com.nhnacademy.heukbaekbookshop.tag.service.TagService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import java.util.Optional;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TagServiceTest {
//
//    @Mock
//    private TagRepository tagRepository;
//
//    @InjectMocks
//    private TagService tagService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void registerTag_Success() {
//        // Given
//        TagCreateRequest request = new TagCreateRequest("newTag");
//        when(tagRepository.findByName(request.name())).thenReturn(Optional.empty());
//
//        // When
//        TagCreateResponse response = tagService.registerTag(request);
//
//        // Then
//        assertNotNull(response);
//        assertEquals("newTag", response.name());
//        verify(tagRepository, times(1)).save(any(Tag.class));
//    }
//
//    @Test
//    void registerTag_AlreadyExists() {
//        // Given
//        TagCreateRequest request = new TagCreateRequest("existingTag");
//        when(tagRepository.findByName(request.name())).thenReturn(Optional.of(new Tag()));
//
//        // When & Then
//        assertThrows(TagAlreadyExistsException.class, () -> tagService.registerTag(request));
//        verify(tagRepository, never()).save(any(Tag.class));
//    }
//
//    @Test
//    void updateTag_Success() {
//        // Given
//        Long tagId = 1L;
//        TagUpdateRequest request = new TagUpdateRequest("updatedTag");
//        Tag existingTag = new Tag();
//        existingTag.setId(tagId);
//        existingTag.setName("oldTag");
//
//        when(tagRepository.findById(tagId)).thenReturn(Optional.of(existingTag));
//
//        // When
//        TagUpdateResponse response = tagService.updateTag(tagId, request);
//
//        // Then
//        assertNotNull(response);
//        assertEquals("updatedTag", response.name());
//        verify(tagRepository, times(1)).save(existingTag);
//    }
//
//    @Test
//    void updateTag_NotFound() {
//        // Given
//        Long tagId = 1L;
//        TagUpdateRequest request = new TagUpdateRequest("updatedTag");
//        when(tagRepository.findById(tagId)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(TagNotFoundException.class, () -> tagService.updateTag(tagId, request));
//        verify(tagRepository, never()).save(any(Tag.class));
//    }
//
//    @Test
//    void deleteTag_Success() {
//        // Given
//        Long tagId = 1L;
//        Tag existingTag = new Tag();
//        existingTag.setId(tagId);
//        existingTag.setName("tagToDelete");
//
//        when(tagRepository.findById(tagId)).thenReturn(Optional.of(existingTag));
//
//        // When
//        TagDeleteResponse response = tagService.deleteTag(tagId);
//
//        // Then
//        assertNotNull(response);
//        assertEquals(tagId, response.id());
//        assertEquals("tagToDelete", response.name());
//        verify(tagRepository, times(1)).delete(existingTag);
//    }
//
//    @Test
//    void deleteTag_NotFound() {
//        // Given
//        Long tagId = 1L;
//        when(tagRepository.findById(tagId)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(TagNotFoundException.class, () -> tagService.deleteTag(tagId));
//        verify(tagRepository, never()).delete(any(Tag.class));
//    }
//
//    @Test
//    void getTag_Success() {
//        // Given
//        Long tagId = 1L;
//        Tag existingTag = new Tag();
//        existingTag.setId(tagId);
//        existingTag.setName("existingTag");
//
//        when(tagRepository.findById(tagId)).thenReturn(Optional.of(existingTag));
//
//        // When
//        TagDetailResponse response = tagService.getTag(tagId);
//
//        // Then
//        assertNotNull(response);
//        assertEquals("existingTag", response.name());
//    }
//
//    @Test
//    void getTag_NotFound() {
//        // Given
//        Long tagId = 1L;
//        when(tagRepository.findById(tagId)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(TagNotFoundException.class, () -> tagService.getTag(tagId));
//    }
//}
