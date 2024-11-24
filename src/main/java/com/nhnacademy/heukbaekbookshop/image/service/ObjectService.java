package com.nhnacademy.heukbaekbookshop.image.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.nhnacademy.heukbaekbookshop.image.domain.PhotoType;
import com.nhnacademy.heukbaekbookshop.image.domain.TokenInfo;
import lombok.NonNull;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ObjectService {
    private final RestTemplate restTemplate;
    private final TokenService tokenService;

    @Value("${object.storage.url}")
    private String storageUrl;

    private TokenInfo token;

    @Autowired
    public ObjectService(TokenService tokenService, RestTemplate restTemplate) {
        this.tokenService = tokenService;
        this.restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        this.token = tokenService.requestToken();
    }

    /**
     * 사진을 업로드하고 URL을 반환하는 메서드.
     *
     * @param file         업로드할 파일 (MultipartFile)
     * @param photoType    사진의 종류 (enum: THUMBNAIL, REVIEW)
     * @return 업로드된 사진의 URL
     */
    public String uploadPhoto(MultipartFile file, PhotoType photoType) {
        // 파일 이름에 UUID를 추가
        String objectName = UUID.randomUUID().toString();
        String containerName = getContainerName(photoType);
        String url = this.getUrl(containerName, objectName);

        // 토큰 갱신 필요 시 재요청
        if (Objects.isNull(token) || token.getExpires().isBefore(LocalDateTime.now())) {
            token = tokenService.requestToken();
        }

        try (InputStream inputStream = file.getInputStream()) {
            // 파일 업로드를 위한 RequestCallback
            final RequestCallback requestCallback = request -> {
                request.getHeaders().add("X-Auth-Token", token.getId());
                IOUtils.copy(inputStream, request.getBody());
            };

            // RestTemplate를 사용하여 파일 업로드
            restTemplate.execute(url, HttpMethod.PUT, requestCallback, null);

            return url; // 업로드된 파일의 URL 반환
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
    }

    private String getUrl(@NonNull String containerName, @NonNull String objectName) {
        return storageUrl + "/" + containerName + "/" + objectName;
    }

    private String getContainerName(PhotoType photoType) {
        // PhotoType에 따라 다른 컨테이너 이름을 반환
        switch (photoType) {
            case THUMBNAIL:
                return "thumbnail";
            case REVIEW:
                return "review";
            default:
                throw new IllegalArgumentException("지원하지 않는 PhotoType입니다: " + photoType);
        }
    }
}
