package com.nhnacademy.heukbaekbookshop.common.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
    private Access access;

    @Getter
    @Setter
    public static class Access {
        private Token token;

        @Getter
        @Setter
        public static class Token {
            private String id;  // 인증 토큰
        }
    }
}
