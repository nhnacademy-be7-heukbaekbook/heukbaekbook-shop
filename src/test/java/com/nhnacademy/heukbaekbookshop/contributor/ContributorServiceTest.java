package com.nhnacademy.heukbaekbookshop.contributor;

import com.nhnacademy.heukbaekbookshop.contributor.domain.Contributor;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.ContributorCreateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.ContributorUpdateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorCreateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorDeleteResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorDetailResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorUpdateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.exception.ContributorAlreadyExistException;
import com.nhnacademy.heukbaekbookshop.contributor.exception.ContributorNotFoundException;
import com.nhnacademy.heukbaekbookshop.contributor.repository.ContributorRepository;
import com.nhnacademy.heukbaekbookshop.contributor.service.ContributorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContributorServiceTest {

    @Mock
    private ContributorRepository contributorRepository;

    @InjectMocks
    private ContributorService contributorService;

    private Contributor contributor;

    @BeforeEach
    public void setUp() {
        contributor = new Contributor();
        contributor.setId(1L);
        contributor.setName("John Doe");
        contributor.setDescription("Author of fantasy novels");
    }

    @Test
    public void registerContributor_Success() {
        // Given
        ContributorCreateRequest request = new ContributorCreateRequest("Jane Smith", "Mystery Writer");

        when(contributorRepository.findByNameAndDescription(request.name(), request.description())).thenReturn(Optional.empty());

        Contributor savedContributor = new Contributor();
        savedContributor.setId(2L);
        savedContributor.setName(request.name());
        savedContributor.setDescription(request.description());

        when(contributorRepository.save(any(Contributor.class))).thenReturn(savedContributor);

        // When
        ContributorCreateResponse response = contributorService.registerContributor(request);

        // Then
        assertNotNull(response);
        assertEquals(request.name(), response.name());
        assertEquals(request.description(), response.description());

        verify(contributorRepository, times(1)).findByNameAndDescription(request.name(), request.description());
        verify(contributorRepository, times(1)).save(any(Contributor.class));
    }

    @Test
    public void registerContributor_ContributorAlreadyExists() {
        // Given
        ContributorCreateRequest request = new ContributorCreateRequest("John Doe", "Author of fantasy novels");

        when(contributorRepository.findByNameAndDescription(request.name(), request.description())).thenReturn(Optional.of(contributor));

        // When & Then
        ContributorAlreadyExistException exception = assertThrows(ContributorAlreadyExistException.class, () -> {
            contributorService.registerContributor(request);
        });

        assertEquals("Contributor already exists", exception.getMessage());

        verify(contributorRepository, times(1)).findByNameAndDescription(request.name(), request.description());
        verify(contributorRepository, never()).save(any(Contributor.class));
    }

    @Test
    public void getContributor_Success() {
        // Given
        Long contributorId = 1L;
        when(contributorRepository.findById(contributorId)).thenReturn(Optional.of(contributor));

        // When
        ContributorDetailResponse response = contributorService.getContributor(contributorId);

        // Then
        assertNotNull(response);
        assertEquals(contributor.getName(), response.name());
        assertEquals(contributor.getDescription(), response.description());

        verify(contributorRepository, times(1)).findById(contributorId);
    }

    @Test
    public void getContributor_NotFound() {
        // Given
        Long contributorId = 2L;
        when(contributorRepository.findById(contributorId)).thenReturn(Optional.empty());

        // When & Then
        ContributorNotFoundException exception = assertThrows(ContributorNotFoundException.class, () -> {
            contributorService.getContributor(contributorId);
        });

        assertEquals("Contributor not found", exception.getMessage());

        verify(contributorRepository, times(1)).findById(contributorId);
    }

    @Test
    public void updateContributor_Success() {
        // Given
        Long contributorId = 1L;
        ContributorUpdateRequest request = new ContributorUpdateRequest("John Updated", "Updated Description");

        when(contributorRepository.findById(contributorId)).thenReturn(Optional.of(contributor));
        when(contributorRepository.save(any(Contributor.class))).thenReturn(contributor);

        // When
        ContributorUpdateResponse response = contributorService.updateContributor(contributorId, request);

        // Then
        assertNotNull(response);
        assertEquals(request.name(), response.name());
        assertEquals(request.description(), response.description());

        verify(contributorRepository, times(1)).findById(contributorId);
        verify(contributorRepository, times(1)).save(contributor);
    }

    @Test
    public void updateContributor_NotFound() {
        // Given
        Long contributorId = 2L;
        ContributorUpdateRequest request = new ContributorUpdateRequest("John Updated", "Updated Description");

        when(contributorRepository.findById(contributorId)).thenReturn(Optional.empty());

        // When & Then
        ContributorNotFoundException exception = assertThrows(ContributorNotFoundException.class, () -> {
            contributorService.updateContributor(contributorId, request);
        });

        assertEquals("Contributor not found", exception.getMessage());

        verify(contributorRepository, times(1)).findById(contributorId);
        verify(contributorRepository, never()).save(any(Contributor.class));
    }

    @Test
    public void deleteContributor_Success() {
        // Given
        Long contributorId = 1L;
        when(contributorRepository.findById(contributorId)).thenReturn(Optional.of(contributor));

        // When
        ContributorDeleteResponse response = contributorService.deleteContributor(contributorId);

        // Then
        assertNotNull(response);
        assertEquals(contributor.getName(), response.name());

        verify(contributorRepository, times(1)).findById(contributorId);
        verify(contributorRepository, times(1)).deleteById(contributorId);
    }

    @Test
    public void deleteContributor_NotFound() {
        // Given
        Long contributorId = 2L;
        when(contributorRepository.findById(contributorId)).thenReturn(Optional.empty());

        // When & Then
        ContributorNotFoundException exception = assertThrows(ContributorNotFoundException.class, () -> {
            contributorService.deleteContributor(contributorId);
        });

        assertEquals("Contributor not found", exception.getMessage());

        verify(contributorRepository, times(1)).findById(contributorId);
        verify(contributorRepository, never()).deleteById(anyLong());
    }
}
