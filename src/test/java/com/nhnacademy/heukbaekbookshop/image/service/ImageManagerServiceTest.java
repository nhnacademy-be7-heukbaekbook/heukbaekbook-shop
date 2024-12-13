package com.nhnacademy.heukbaekbookshop.image.service;

import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ImageManagerServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ImageManagerProperties properties;

    @InjectMocks
    private ImageManagerService imageManagerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private String invokeGetFileExtension(String fileName) throws Exception {
        Method method = ImageManagerService.class.getDeclaredMethod("getFileExtension", String.class);
        method.setAccessible(true);
        return (String) method.invoke(imageManagerService, fileName);
    }

    @Test
    void uploadPhoto_Success() throws IOException {
        // Given
        String url = "http://example.com";
        String secretId = "secretId";
        when(properties.getUrl()).thenReturn(url);
        when(properties.getSecretId()).thenReturn(secretId);

        String originalFileName = "test.jpg";
        String fileContent = "dummy image content";
        MockMultipartFile file = new MockMultipartFile("file", originalFileName, "image/jpeg", fileContent.getBytes(StandardCharsets.UTF_8));

        // Mock RestTemplate
        when(restTemplate.execute(anyString(), any(HttpMethod.class), any(RequestCallback.class), any(ResponseExtractor.class))).thenReturn(null);

        // When
        String result = imageManagerService.uploadPhoto(file, ImageType.THUMBNAIL);

        // Then
        assertNotNull(result);
        assertTrue(result.contains("/heukbaekbook/thumbnail/"));

        // Verify RestTemplate.execute() was called with correct parameters
        verify(restTemplate, times(1)).execute(anyString(), eq(HttpMethod.PUT), any(RequestCallback.class), eq(null));
    }

    @Test
    void uploadPhoto_InvalidFileName() {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            imageManagerService.uploadPhoto(file, ImageType.THUMBNAIL);
        });
        assertEquals("파일 이름이 유효하지 않습니다.", exception.getMessage());
    }

    @Test
    void uploadPhoto_InvalidExtension() {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("filename"); // 확장자 없음

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            imageManagerService.uploadPhoto(file, ImageType.THUMBNAIL);
        });
        assertEquals("파일 확장자가 유효하지 않습니다.", exception.getMessage());
    }

    @Test
    void uploadPhoto_IOException() throws IOException {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getInputStream()).thenThrow(new IOException("Test IOException"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imageManagerService.uploadPhoto(file, ImageType.THUMBNAIL);
        });
        assertEquals("파일 업로드 실패", exception.getMessage());
        assertEquals("Test IOException", exception.getCause().getMessage());
    }

    // 추가: 프라이빗 메서드 getFileExtension 테스트
    @Test
    void testGetFileExtension_ValidExtensions() throws Exception {
        // Given
        String fileName1 = "image.jpg";
        String fileName2 = "photo.PNG";
        String fileName3 = "archive.tar.gz";
        String fileName4 = "no_extension";

        // When
        String extension1 = invokeGetFileExtension(fileName1);
        String extension2 = invokeGetFileExtension(fileName2);
        String extension3 = invokeGetFileExtension(fileName3);
        String extension4 = invokeGetFileExtension(fileName4);

        // Then
        assertEquals("jpg", extension1);
        assertEquals("png", extension2);
        assertEquals("gz", extension3); // 마지막 확장자만 추출
        assertNull(extension4);
    }

    @Test
    void testGetFileExtension_InvalidFileNames() throws Exception {
        // Given
        String fileName1 = ".hiddenfile";
        String fileName2 = "file.";
        String fileName3 = "file..txt";

        // When
        String extension1 = invokeGetFileExtension(fileName1);
        String extension2 = invokeGetFileExtension(fileName2);
        String extension3 = invokeGetFileExtension(fileName3);

        // Then
        assertNull(extension1); // 앞에 점만 있음
        assertNull(extension2); // 끝에 점만 있음
        assertEquals("txt", extension3); // 마지막 확장자만 추출
    }
}