package com.nhnacademy.heukbaekbookshop.image.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenRequest {
    private Auth auth;

    @Getter
    @AllArgsConstructor
    public static class Auth {
        private String tenantId;
        private PasswordCredentials passwordCredentials;
    }

    @Getter
    @AllArgsConstructor
    public static class PasswordCredentials {
        private String username;
        private String password;
    }
}
