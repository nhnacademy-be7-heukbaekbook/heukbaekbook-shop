package com.nhnacademy.heukbaekbookshop.order.controller;

import com.nhnacademy.heukbaekbookshop.order.dto.response.WrappingPaperResponse;
import com.nhnacademy.heukbaekbookshop.order.service.WrappingPaperService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WrappingPaperController.class)
class WrappingPaperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WrappingPaperService wrappingPaperService;

    @Test
    void getAllWrappingPapers_ShouldReturnListOfWrappingPapers() throws Exception {
        //given
        WrappingPaperResponse paper1 = new WrappingPaperResponse(1L, "Red Paper", BigDecimal.valueOf(500), "url1");
        WrappingPaperResponse paper2 = new WrappingPaperResponse(2L, "Green Paper", BigDecimal.valueOf(1000), "url2");

        List<WrappingPaperResponse> wrappingPapers = Arrays.asList(paper1, paper2);

        //when
        when(wrappingPaperService.getAllWrappingPapers()).thenReturn(wrappingPapers);

        //then
        mockMvc.perform(get("/api/wrapping-papers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Red Paper"))
                .andExpect(jsonPath("$[0].price").value(BigDecimal.valueOf(500)))
                .andExpect(jsonPath("$[0].imageUrl").value("url1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Green Paper"))
                .andExpect(jsonPath("$[1].price").value(BigDecimal.valueOf(1000)))
                .andExpect(jsonPath("$[1].imageUrl").value("url2"));
    }
}