//package com.nhnacademy.heukbaekbookshop.publisher;
//
//import com.nhnacademy.heukbaekbookshop.contributor.domain.Publisher;
//import com.nhnacademy.heukbaekbookshop.contributor.dto.request.PublisherCreateRequest;
//import com.nhnacademy.heukbaekbookshop.contributor.dto.request.PublisherUpdateRequest;
//import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherCreateResponse;
//import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherDeleteResponse;
//import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherDetailResponse;
//import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherUpdateResponse;
//import com.nhnacademy.heukbaekbookshop.contributor.exception.PublisherAlreadyExistException;
//import com.nhnacademy.heukbaekbookshop.contributor.exception.PublisherNotFoundException;
//import com.nhnacademy.heukbaekbookshop.contributor.repository.PublisherRepository;
//import com.nhnacademy.heukbaekbookshop.contributor.service.PublisherService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//class PublisherServiceTest {
//
//    @InjectMocks
//    private PublisherService publisherService;
//
//    @Mock
//    private PublisherRepository publisherRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testRegisterPublisherAlreadyExists() {
//        PublisherCreateRequest request = new PublisherCreateRequest("Existing Publisher");
//        when(publisherRepository.findByName(request.name())).thenReturn(Optional.of(new Publisher()));
//
//        assertThrows(PublisherAlreadyExistException.class, () -> publisherService.registerPublisher(request));
//    }
//
//    @Test
//    void testGetPublisherNotFound() {
//        when(publisherRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(PublisherNotFoundException.class, () -> publisherService.getPublisher(1L));
//    }
//
//    @Test
//    void testUpdatePublisherNotFound() {
//        PublisherUpdateRequest request = new PublisherUpdateRequest("Updated Publisher");
//        when(publisherRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(PublisherNotFoundException.class, () -> publisherService.updatePublisher(1L, request));
//    }
//
//    @Test
//    void testDeletePublisherNotFound() {
//        when(publisherRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(PublisherNotFoundException.class, () -> publisherService.deletePublisher(1L));
//    }
//}
