package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.nhnacademy.heukbaekbookshop.image.service.ImageManagerService;
import com.nhnacademy.heukbaekbookshop.order.domain.WrappingPaper;
import com.nhnacademy.heukbaekbookshop.image.domain.WrappingPaperImage;
import com.nhnacademy.heukbaekbookshop.order.repository.WrappingPaperImageRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.WrappingPaperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WrappingPaperAdminServiceTest {

    @Mock
    private WrappingPaperRepository wrappingPaperRepository;

    @Mock
    private WrappingPaperImageRepository wrappingPaperImageRepository;

    @Mock
    private ImageManagerService imageManagerService;

    @InjectMocks
    private WrappingPaperAdminService wrappingPaperAdminService;

    private WrappingPaper existingWrappingPaper;
    private WrappingPaperImage existingWrappingPaperImage;

    @BeforeEach
    void setUp() {
        existingWrappingPaper = new WrappingPaper();
        existingWrappingPaper.setId(1L);
        existingWrappingPaper.setName("Existing Wrapping Paper");
        existingWrappingPaper.setPrice(new BigDecimal("10.00"));

        existingWrappingPaperImage = new WrappingPaperImage();
        existingWrappingPaperImage.setId(1L);
        existingWrappingPaperImage.setWrappingPaper(existingWrappingPaper);
        existingWrappingPaperImage.setUrl("http://example.com/image.jpg");
        existingWrappingPaperImage.setType(ImageType.WRAPPING_PAPER);
    }

    @Test
    @DisplayName("포장지 등록 - 이미지 포함")
    void addWrappingPaper_WithImage() {
        String name = "New Wrapping Paper";
        BigDecimal price = new BigDecimal("15.00");
        MultipartFile imageFile = mock(MultipartFile.class);
        String imageUrl = "http://example.com/new-image.jpg";

        // Mocking behaviors
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageManagerService.uploadPhoto(imageFile, ImageType.WRAPPING_PAPER)).thenReturn(imageUrl);
        when(wrappingPaperRepository.save(any(WrappingPaper.class))).thenAnswer(invocation -> {
            WrappingPaper wp = invocation.getArgument(0);
            wp.setId(2L); // Simulate DB assigning ID
            return wp;
        });
        when(wrappingPaperImageRepository.save(any(WrappingPaperImage.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute the method
        WrappingPaper result = wrappingPaperAdminService.addWrappingPaper(name, price, imageFile);

        // Verify interactions
        verify(wrappingPaperRepository, times(1)).save(any(WrappingPaper.class));
        verify(imageManagerService, times(1)).uploadPhoto(imageFile, ImageType.WRAPPING_PAPER);
        verify(wrappingPaperImageRepository, times(1)).save(any(WrappingPaperImage.class));

        // Assertions
        assertNotNull(result.getId(), "포장지 ID는 null이 아니어야 합니다.");
        assertEquals(name, result.getName(), "포장지 이름이 일치해야 합니다.");
        assertEquals(price, result.getPrice(), "포장지 가격이 일치해야 합니다.");
    }

    @Test
    @DisplayName("포장지 등록 - 이미지 미포함")
    void addWrappingPaper_WithoutImage() {
        String name = "New Wrapping Paper";
        BigDecimal price = new BigDecimal("15.00");
        MultipartFile imageFile = mock(MultipartFile.class);

        // Mocking behaviors
        when(imageFile.isEmpty()).thenReturn(true);
        when(wrappingPaperRepository.save(any(WrappingPaper.class))).thenAnswer(invocation -> {
            WrappingPaper wp = invocation.getArgument(0);
            wp.setId(2L); // Simulate DB assigning ID
            return wp;
        });

        // Execute the method
        WrappingPaper result = wrappingPaperAdminService.addWrappingPaper(name, price, imageFile);

        // Verify interactions
        verify(wrappingPaperRepository, times(1)).save(any(WrappingPaper.class));
        verify(imageManagerService, times(0)).uploadPhoto(any(MultipartFile.class), any(ImageType.class));
        verify(wrappingPaperImageRepository, times(0)).save(any(WrappingPaperImage.class));

        // Assertions
        assertNotNull(result.getId(), "포장지 ID는 null이 아니어야 합니다.");
        assertEquals(name, result.getName(), "포장지 이름이 일치해야 합니다.");
        assertEquals(price, result.getPrice(), "포장지 가격이 일치해야 합니다.");
    }

    @Test
    @DisplayName("포장지 수정 - 기존 이미지 포함")
    void updateWrappingPaper_WithExistingImage() {
        Long id = 1L;
        String updatedName = "Updated Wrapping Paper";
        BigDecimal updatedPrice = new BigDecimal("20.00");
        MultipartFile newImageFile = mock(MultipartFile.class);
        String newImageUrl = "http://example.com/updated-image.jpg";

        // Mocking behaviors
        when(wrappingPaperRepository.findById(id)).thenReturn(Optional.of(existingWrappingPaper));
        when(newImageFile.isEmpty()).thenReturn(false);
        when(imageManagerService.uploadPhoto(newImageFile, ImageType.WRAPPING_PAPER)).thenReturn(newImageUrl);
        when(wrappingPaperImageRepository.findByWrappingPaper(existingWrappingPaper)).thenReturn(Optional.of(existingWrappingPaperImage));
        when(wrappingPaperRepository.save(existingWrappingPaper)).thenReturn(existingWrappingPaper);
        when(wrappingPaperImageRepository.save(any(WrappingPaperImage.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute the method
        WrappingPaper result = wrappingPaperAdminService.updateWrappingPaper(id, updatedName, updatedPrice, newImageFile);

        // Verify interactions
        verify(wrappingPaperRepository, times(1)).findById(id);
        verify(wrappingPaperRepository, times(1)).save(existingWrappingPaper);
        verify(imageManagerService, times(1)).uploadPhoto(newImageFile, ImageType.WRAPPING_PAPER);
        verify(wrappingPaperImageRepository, times(1)).findByWrappingPaper(existingWrappingPaper);
        verify(wrappingPaperImageRepository, times(1)).save(existingWrappingPaperImage);

        // Assertions
        assertEquals(updatedName, result.getName(), "포장지 이름이 업데이트되어야 합니다.");
        assertEquals(updatedPrice, result.getPrice(), "포장지 가격이 업데이트되어야 합니다.");
        assertEquals(newImageUrl, existingWrappingPaperImage.getUrl(), "이미지 URL이 업데이트되어야 합니다.");
    }

    @Test
    @DisplayName("포장지 수정 - 기존 이미지 미포함")
    void updateWrappingPaper_WithoutExistingImage() {
        Long id = 1L;
        String updatedName = "Updated Wrapping Paper";
        BigDecimal updatedPrice = new BigDecimal("20.00");
        MultipartFile newImageFile = mock(MultipartFile.class);
        String newImageUrl = "http://example.com/updated-image.jpg";

        // Mocking behaviors
        when(wrappingPaperRepository.findById(id)).thenReturn(Optional.of(existingWrappingPaper));
        when(newImageFile.isEmpty()).thenReturn(false);
        when(imageManagerService.uploadPhoto(newImageFile, ImageType.WRAPPING_PAPER)).thenReturn(newImageUrl);
        when(wrappingPaperImageRepository.findByWrappingPaper(existingWrappingPaper)).thenReturn(Optional.empty());
        when(wrappingPaperRepository.save(existingWrappingPaper)).thenReturn(existingWrappingPaper);
        when(wrappingPaperImageRepository.save(any(WrappingPaperImage.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute the method
        WrappingPaper result = wrappingPaperAdminService.updateWrappingPaper(id, updatedName, updatedPrice, newImageFile);

        // Verify interactions
        verify(wrappingPaperRepository, times(1)).findById(id);
        verify(wrappingPaperRepository, times(1)).save(existingWrappingPaper);
        verify(imageManagerService, times(1)).uploadPhoto(newImageFile, ImageType.WRAPPING_PAPER);
        verify(wrappingPaperImageRepository, times(1)).findByWrappingPaper(existingWrappingPaper);
        verify(wrappingPaperImageRepository, times(1)).save(any(WrappingPaperImage.class));

        // Assertions
        assertEquals(updatedName, result.getName(), "포장지 이름이 업데이트되어야 합니다.");
        assertEquals(updatedPrice, result.getPrice(), "포장지 가격이 업데이트되어야 합니다.");
    }

    @Test
    @DisplayName("포장지 수정 - 포장지 존재하지 않음")
    void updateWrappingPaper_NotFound() {
        Long id = 2L;
        String updatedName = "Updated Wrapping Paper";
        BigDecimal updatedPrice = new BigDecimal("20.00");
        MultipartFile newImageFile = mock(MultipartFile.class);

        // Mocking behaviors
        when(wrappingPaperRepository.findById(id)).thenReturn(Optional.empty());

        // Execute and assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            wrappingPaperAdminService.updateWrappingPaper(id, updatedName, updatedPrice, newImageFile);
        }, "존재하지 않는 포장지에 대해 예외가 발생해야 합니다.");

        assertEquals("Wrapping paper not found", exception.getMessage(), "예외 메시지가 일치해야 합니다.");
    }

    @Test
    @DisplayName("포장지 삭제 - 포장지 존재")
    void deleteWrappingPaper_Exists() {
        Long id = 1L;

        // Mocking behaviors
        when(wrappingPaperRepository.findById(id)).thenReturn(Optional.of(existingWrappingPaper));
        doNothing().when(wrappingPaperImageRepository).deleteByWrappingPaper(existingWrappingPaper);
        doNothing().when(wrappingPaperRepository).deleteById(id);

        // Execute the method
        wrappingPaperAdminService.deleteWrappingPaper(id);

        // Verify interactions
        verify(wrappingPaperRepository, times(1)).findById(id);
        verify(wrappingPaperImageRepository, times(1)).deleteByWrappingPaper(existingWrappingPaper);
        verify(wrappingPaperRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("포장지 삭제 - 포장지 존재하지 않음")
    void deleteWrappingPaper_NotFound() {
        Long id = 2L;

        // Mocking behaviors
        when(wrappingPaperRepository.findById(id)).thenReturn(Optional.empty());

        // Execute and assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            wrappingPaperAdminService.deleteWrappingPaper(id);
        }, "존재하지 않는 포장지에 대해 예외가 발생해야 합니다.");

        assertEquals("Wrapping paper not found", exception.getMessage(), "예외 메시지가 일치해야 합니다.");
    }

    @Test
    @DisplayName("포장지 수정 - 이미지 파일이 비어있음")
    void updateWrappingPaper_ImageFileEmpty() {
        Long id = 1L;
        String updatedName = "Updated Wrapping Paper";
        BigDecimal updatedPrice = new BigDecimal("20.00");
        MultipartFile newImageFile = mock(MultipartFile.class);

        // Mocking behaviors
        when(wrappingPaperRepository.findById(id)).thenReturn(Optional.of(existingWrappingPaper));
        when(newImageFile.isEmpty()).thenReturn(true);
        when(wrappingPaperRepository.save(existingWrappingPaper)).thenReturn(existingWrappingPaper);

        // Execute the method
        WrappingPaper result = wrappingPaperAdminService.updateWrappingPaper(id, updatedName, updatedPrice, newImageFile);

        // Verify interactions
        verify(wrappingPaperRepository, times(1)).findById(id);
        verify(wrappingPaperRepository, times(1)).save(existingWrappingPaper);
        verify(imageManagerService, times(0)).uploadPhoto(any(MultipartFile.class), any(ImageType.class));
        verify(wrappingPaperImageRepository, times(0)).findByWrappingPaper(existingWrappingPaper);
        verify(wrappingPaperImageRepository, times(0)).save(any(WrappingPaperImage.class));

        // Assertions
        assertEquals(updatedName, result.getName(), "포장지 이름이 업데이트되어야 합니다.");
        assertEquals(updatedPrice, result.getPrice(), "포장지 가격이 업데이트되어야 합니다.");
    }
}