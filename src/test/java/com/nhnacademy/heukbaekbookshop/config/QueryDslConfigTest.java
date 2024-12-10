package com.nhnacademy.heukbaekbookshop.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {QueryDslConfig.class, QueryDslConfigTest.TestConfig.class})
class QueryDslConfigTest {

    @Configuration
    static class TestConfig {
        @Bean
        public EntityManager entityManager() {
            // Mock된 EntityManager를 사용하여 DB 연결 없이 테스트
            return Mockito.mock(EntityManager.class);
        }
    }

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Test
    void testJPAQueryFactoryBean() {
        // JPAQueryFactory 빈이 정상적으로 로딩되고 주입되었는지 검증
        assertThat(jpaQueryFactory).isNotNull();
    }
}