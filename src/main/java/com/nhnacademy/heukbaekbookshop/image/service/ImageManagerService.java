package com.nhnacademy.heukbaekbookshop.image.service;

import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageManagerService {
    private final RestTemplate restTemplate;
    private final ImageManagerProperties properties;

    public String uploadPhoto(MultipartFile file, ImageType imageType) {
        // 기본 경로 설정
        String baseFolder = "/heukbaekbook/";

        // 파일 확장자 추출
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IllegalArgumentException("파일 이름이 유효하지 않습니다.");
        }
        String extension = getFileExtension(originalFileName);
        if (extension == null || extension.isEmpty()) {
            throw new IllegalArgumentException("파일 확장자가 유효하지 않습니다.");
        }

        String containerPath = baseFolder + imageType.name().toLowerCase();
        String uniqueFileName = imageType.name().toLowerCase() + UUID.randomUUID().toString().replace("-", "").substring(0, 8) + "." + extension;

        // URL Encoding
        String encodedFileName = URLEncoder.encode(uniqueFileName, StandardCharsets.UTF_8);
        String uploadUrl = properties.getUrl() + "/images?path=" + containerPath + "/" + encodedFileName + "&overwrite=true";

        // HTTP Headers 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", properties.getSecretId());
        headers.add("Content-Type", "application/octet-stream");

        try (InputStream inputStream = file.getInputStream()) {
            RequestCallback requestCallback = request -> {
                request.getHeaders().addAll(headers);
                IOUtils.copy(inputStream, request.getBody());
            };

            // 파일 업로드 요청
            restTemplate.execute(uploadUrl, HttpMethod.PUT, requestCallback, null);

            String publicUrl = "http://image.toast.com/aaaacko" + containerPath + "/" + uniqueFileName;
            return publicUrl;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    // 파일 확장자 추출 메서드
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return null;
    }
}
