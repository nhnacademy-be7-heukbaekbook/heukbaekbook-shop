package com.nhnacademy.heukbaekbookshop.common.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    private final String identityUrl = "https://api-identity-infrastructure.nhncloudservice.com/v2.0";
    private final RestTemplate restTemplate;

    public AuthService() {
        this.restTemplate = new RestTemplate();
    }

    public String requestToken(String tenantId, String username, String password) {
        String url = identityUrl + "/tokens";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = String.format(
                "{ \"auth\": { \"tenantId\": \"%s\", \"passwordCredentials\": { \"username\": \"%s\", \"password\": \"%s\" } } }",
                tenantId, username, password
        );

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                TokenResponse tokenResponse = objectMapper.readValue(response.getBody(), TokenResponse.class);
                return tokenResponse.getAccess().getToken().getId();
            } catch (Exception e) {
                throw new RuntimeException("토큰 응답 매핑 실패", e);
            }
        } else {
            throw new RuntimeException("토큰 요청 실패: " + response.getStatusCode());
        }
    }
}
