package com.nhnacademy.heukbaekbookshop.image;

import com.nhnacademy.heukbaekbookshop.image.ImageManagerProperties;
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

@Service
@RequiredArgsConstructor
public class ImageManagerService {
    private final RestTemplate restTemplate;
    private final ImageManagerProperties properties;

    public String uploadPhoto(MultipartFile file, ImageType imageType) {
        String containerPath = "/heukbaekbook/" + imageType.name().toLowerCase();
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("파일 이름이 유효하지 않습니다.");
        }

        // URL Encoding
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
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
            return properties.getUrl() + containerPath + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }
}
