//package com.nhnacademy.heukbaekbookshop.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf((auth) -> auth.disable());
//        http.formLogin((auth) -> auth.disable());
//        http.httpBasic((auth) -> auth.disable());
//        http.authorizeRequests().anyRequest().permitAll();
//
//        return http.build();
//    }
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//
//        return new BCryptPasswordEncoder();
//    }
//
//}
//
//
//
