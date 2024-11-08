//package com.nhnacademy.heukbaekbookshop.publisher;
//
//import com.nhnacademy.heukbaekbookshop.contributor.controller.PublisherController;
//import com.nhnacademy.heukbaekbookshop.contributor.dto.request.PublisherCreateRequest;
//import com.nhnacademy.heukbaekbookshop.contributor.dto.request.PublisherUpdateRequest;
//import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherCreateResponse;
//import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherDeleteResponse;
//import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherDetailResponse;
//import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherUpdateResponse;
//import com.nhnacademy.heukbaekbookshop.contributor.exception.PublisherAlreadyExistException;
//import com.nhnacademy.heukbaekbookshop.contributor.exception.PublisherNotFoundException;
//import com.nhnacademy.heukbaekbookshop.contributor.service.PublisherService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class PublisherControllerTest {
//
//    @InjectMocks
//    private PublisherController publisherController;
//
//    @Mock
//    private PublisherService publisherService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testRegisterPublisher() {
//        PublisherCreateRequest request = new PublisherCreateRequest("New Publisher");
//        PublisherCreateResponse response = new PublisherCreateResponse("New Publisher");
//
//        when(publisherService.registerPublisher(request)).thenReturn(response);
//
//        ResponseEntity<PublisherCreateResponse> result = publisherController.registerPublisher(request);
//
//        assertEquals(HttpStatus.CREATED, result.getStatusCode());
//        assertEquals(response, result.getBody());
//    }
//
//    @Test
//    void testGetPublisher() {
//        PublisherDetailResponse response = new PublisherDetailResponse("Existing Publisher");
//
//        when(publisherService.getPublisher(1L)).thenReturn(response);
//
//        ResponseEntity<PublisherDetailResponse> result = publisherController.getPublisher(1L);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(response, result.getBody());
//    }
//
//    @Test
//    void testGetPublisherNotFound() {
//        when(publisherService.getPublisher(1L)).thenThrow(new PublisherNotFoundException("출판사를 찾을 수 없습니다."));
//
//        try {
//            publisherController.getPublisher(1L);
//        } catch (PublisherNotFoundException ex) {
//            assertEquals("출판사를 찾을 수 없습니다.", ex.getMessage());
//        }
//    }
//
//    @Test
//    void testUpdatePublisher() {
//        PublisherUpdateRequest request = new PublisherUpdateRequest("Updated Publisher");
//        PublisherUpdateResponse response = new PublisherUpdateResponse("Updated Publisher");
//
//        when(publisherService.updatePublisher(1L, request)).thenReturn(response);
//
//        ResponseEntity<PublisherUpdateResponse> result = publisherController.updatePublisher(1L, request);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(response, result.getBody());
//    }
//
//    @Test
//    void testUpdatePublisherNotFound() {
//        PublisherUpdateRequest request = new PublisherUpdateRequest("Updated Publisher");
//
//        when(publisherService.updatePublisher(1L, request)).thenThrow(new PublisherNotFoundException("출판사를 찾을 수 없습니다."));
//
//        try {
//            publisherController.updatePublisher(1L, request);
//        } catch (PublisherNotFoundException ex) {
//            assertEquals("출판사를 찾을 수 없습니다.", ex.getMessage());
//        }
//    }
//
//    @Test
//    void testDeletePublisher() {
//        PublisherDeleteResponse response = new PublisherDeleteResponse("Deleted Publisher");
//
//        when(publisherService.deletePublisher(1L)).thenReturn(response);
//
//        ResponseEntity<PublisherDeleteResponse> result = publisherController.deletePublisher(1L);
//
//        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
//    }
//
//    @Test
//    void testDeletePublisherNotFound() {
//        when(publisherService.deletePublisher(1L)).thenThrow(new PublisherNotFoundException("출판사를 찾을 수 없습니다."));
//
//        try {
//            publisherController.deletePublisher(1L);
//        } catch (PublisherNotFoundException ex) {
//            assertEquals("출판사를 찾을 수 없습니다.", ex.getMessage());
//        }
//    }
//}
