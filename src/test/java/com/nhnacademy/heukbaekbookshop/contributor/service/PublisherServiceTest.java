package com.nhnacademy.heukbaekbookshop.contributor.service;

import com.nhnacademy.heukbaekbookshop.contributor.domain.Publisher;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.PublisherCreateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.PublisherUpdateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherCreateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherDeleteResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherDetailResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherUpdateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.exception.PublisherAlreadyExistException;
import com.nhnacademy.heukbaekbookshop.contributor.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PublisherServiceTest {

    @InjectMocks
    private PublisherService publisherService;

    @Mock
    private PublisherRepository publisherRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterPublisher_Success() {
        // Given
        PublisherCreateRequest request = new PublisherCreateRequest("New Publisher");
        when(publisherRepository.findByName("New Publisher")).thenReturn(Optional.empty());

        Publisher mockPublisher = new Publisher();
        mockPublisher.setName("New Publisher");
        when(publisherRepository.save(any(Publisher.class))).thenReturn(mockPublisher);

        // When
        PublisherCreateResponse response = publisherService.registerPublisher(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("New Publisher");

        verify(publisherRepository, times(1)).findByName("New Publisher");
        verify(publisherRepository, times(1)).save(any(Publisher.class));
    }

    @Test
    void testRegisterPublisher_AlreadyExists() {
        // Given
        PublisherCreateRequest request = new PublisherCreateRequest("Existing Publisher");
        Publisher existingPublisher = new Publisher();
        existingPublisher.setName("Existing Publisher");
        when(publisherRepository.findByName("Existing Publisher")).thenReturn(Optional.of(existingPublisher));

        // When / Then
        assertThrows(PublisherAlreadyExistException.class, () -> publisherService.registerPublisher(request));

        verify(publisherRepository, times(1)).findByName("Existing Publisher");
        verify(publisherRepository, never()).save(any(Publisher.class));
    }

    @Test
    void testGetPublishers() {
        // Given
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        Publisher publisher1 = new Publisher(1L, "Publisher One");
        Publisher publisher2 = new Publisher(2L, "Publisher Two");
        Page<Publisher> mockPublishers = new PageImpl<>(List.of(publisher1, publisher2));

        when(publisherRepository.findAll(pageable)).thenReturn(mockPublishers);

        // When
        Page<PublisherDetailResponse> result = publisherService.getPublishers(pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());

        PublisherDetailResponse response1 = result.getContent().get(0);
        assertEquals(1L, response1.id());
        assertEquals("Publisher One", response1.name());

        PublisherDetailResponse response2 = result.getContent().get(1);
        assertEquals(2L, response2.id());
        assertEquals("Publisher Two", response2.name());

        verify(publisherRepository, times(1)).findAll(pageable);
    }

    @Test
    void testUpdatePublisher() {
        // Given
        Long publisherId = 1L;
        PublisherUpdateRequest request = new PublisherUpdateRequest("Updated Publisher Name");

        Publisher existingPublisher = new Publisher(publisherId, "Old Publisher Name");

        when(publisherRepository.findById(publisherId)).thenReturn(Optional.of(existingPublisher));
        when(publisherRepository.save(any(Publisher.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        PublisherUpdateResponse response = publisherService.updatePublisher(publisherId, request);

        // Then
        assertNotNull(response);
        assertEquals("Updated Publisher Name", response.name());

        verify(publisherRepository, times(2)).findById(publisherId);
        verify(publisherRepository, times(1)).save(existingPublisher);
    }

    @Test
    void testDeletePublisher() {
        // Given
        Long publisherId = 1L;
        Publisher existingPublisher = new Publisher(publisherId, "Test Publisher");

        when(publisherRepository.findById(publisherId)).thenReturn(Optional.of(existingPublisher));

        // When
        PublisherDeleteResponse response = publisherService.deletePublisher(publisherId);

        // Then
        assertNotNull(response);
        assertEquals("Test Publisher", response.name());

        verify(publisherRepository, times(2)).findById(publisherId);
        verify(publisherRepository, times(1)).delete(existingPublisher);
    }

    @Test
    void testGetPublisherById_Success() {
        // Given
        Long publisherId = 1L;
        Publisher existingPublisher = new Publisher(publisherId, "Test Publisher");

        when(publisherRepository.findById(publisherId)).thenReturn(Optional.of(existingPublisher));

        // When
        PublisherDetailResponse response = publisherService.getPublisherById(publisherId);

        // Then
        assertNotNull(response);
        assertEquals(publisherId, response.id());
        assertEquals("Test Publisher", response.name());

        verify(publisherRepository, times(2)).findById(publisherId);
    }
}
