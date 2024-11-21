package com.nhnacademy.heukbaekbookshop.image.service;

import com.nhnacademy.heukbaekbookshop.common.storage.ContainerService;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageUploadService {
    private final ContainerService containerService;
    private final String containerName = "heukbaekbook";

    public ImageUploadService(ContainerService containerService) {
        this.containerService = containerService;
    }

    /**
     * 이미지 업로드
     *
     * @param tokenId      인증 토큰
     * @param base64Images Base64 인코딩된 이미지 리스트
     * @return 업로드된 이미지 URL 리스트
     */
    public List<String> uploadImages(String tokenId, List<String> base64Images) {
        return base64Images.stream()
                .map(base64Image -> {
                    try {
                        // Base64 디코드
                        byte[] imageData = Base64.getDecoder().decode(base64Image.split(",")[1]);

                        // 이미지 검증
                        validateImage(imageData);

                        // 고유 파일명 생성
                        String objectName = generateFileName();

                        // 업로드 및 URL 반환
                        return containerService.uploadObject(tokenId, containerName, objectName, imageData);
                    } catch (Exception e) {
                        throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다: " + e.getMessage(), e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 이미지 검증 (크기 및 포맷)
     *
     * @param imageData 이미지 바이트 배열
     */
    private void validateImage(byte[] imageData) {
        final int MAX_SIZE = 5 * 1024 * 1024; // 5MB 제한

        // 크기 검증
        if (imageData.length > MAX_SIZE) {
            throw new IllegalArgumentException("이미지 크기가 5MB를 초과합니다.");
        }

        // 형식 검증 (JPEG/PNG만 허용)
        String mimeType = detectMimeType(imageData);
        if (!"image/jpeg".equals(mimeType) && !"image/png".equals(mimeType)) {
            throw new IllegalArgumentException("지원하지 않는 이미지 형식입니다: " + mimeType);
        }
    }

    /**
     * 이미지 MIME 타입 감지
     *
     * @param imageData 이미지 바이트 배열
     * @return MIME 타입 (image/jpeg 또는 image/png)
     */
    private String detectMimeType(byte[] imageData) {
        if (imageData[0] == (byte) 0xFF && imageData[1] == (byte) 0xD8) {
            return "image/jpeg";
        } else if (imageData[0] == (byte) 0x89 && imageData[1] == (byte) 0x50) {
            return "image/png";
        } else {
            return "unknown";
        }
    }

    /**
     * 고유 파일명 생성
     *
     * @return UUID 기반 고유 파일명
     */
    private String generateFileName() {
        return "img_" + UUID.randomUUID();
    }
}
