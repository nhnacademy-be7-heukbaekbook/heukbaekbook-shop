package com.nhnacademy.heukbaekbookshop.common.advice;

import com.nhnacademy.heukbaekbookshop.image.exception.ImageNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = WebControllerAdviceTest.TestController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry.class)
)
@Import(WebControllerAdvice.class)
class WebControllerAdviceTest {

    @Autowired
    MockMvc mockMvc;

    @RestController
    public static class TestController {
        @GetMapping(value = "/api/v1/error-test", produces = MediaType.APPLICATION_JSON_VALUE)
        public String errorTest() {
            throw new ImageNotFoundException("Image not found");
        }
    }

    @Test
    @DisplayName("ImageNotFoundException 발생 시 404 응답과 JSON 에러 메시지 확인")
    void testImageNotFoundException() throws Exception {
        mockMvc.perform(get("/api/v1/error-test")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}