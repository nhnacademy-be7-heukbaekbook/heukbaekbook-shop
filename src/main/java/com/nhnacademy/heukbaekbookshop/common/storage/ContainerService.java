package com.nhnacademy.heukbaekbookshop.common.storage;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ContainerService {
    private final RestTemplate restTemplate;
    private final String storageUrl = "https://kr1-api-object-storage.nhncloudservice.com/v1/AUTH_fcb81f74e379456b8ca0e091d351a7af";

    public ContainerService() {
        this.restTemplate = new RestTemplate();
    }

    public String uploadObject(String tokenId, String containerName, String objectName, byte[] fileData) {
        String url = storageUrl + "/" + containerName + "/" + objectName;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Token", tokenId);
        headers.add("Content-Type", "application/octet-stream");

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(fileData, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return url; // 업로드된 이미지 URL 반환
        } else {
            throw new RuntimeException("이미지 업로드 실패: " + response.getStatusCode());
        }
    }
}
