package com.nhnacademy.heukbaekbookshop.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.nhnacademy.heukbaekbookshop.image.domain.TokenInfo;
import com.nhnacademy.heukbaekbookshop.image.domain.TokenRequest;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${object.storage.auth.url}")
    private String authUrl;

    @Value("${object.storage.tenant.id}")
    private String tenantId;

    @Value("${object.storage.username}")
    private String username;

    @Value("${object.storage.password}")
    private String password;

    private final RestTemplate restTemplate;

    /**
     * 토큰 생성 메서드.
     *
     * @return 생성된 TokenInfo
     */
    public TokenInfo requestToken() {
        // 요청 본문 생성
        TokenRequest tokenRequest = new TokenRequest(
                new TokenRequest.Auth(
                        tenantId,
                        new TokenRequest.PasswordCredentials(username, password)
                )
        );

        // 인증 URL 생성
        String identityUrl = authUrl + "/tokens";

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        // HTTP 요청
        HttpEntity<TokenRequest> requestEntity = new HttpEntity<>(tokenRequest, headers);
        ResponseEntity<TokenInfo> response = restTemplate.exchange(identityUrl, HttpMethod.POST, requestEntity, TokenInfo.class);

        // 토큰 반환
        return Objects.requireNonNull(response.getBody());
    }
}
