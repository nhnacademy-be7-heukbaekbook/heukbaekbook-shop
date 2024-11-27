package com.nhnacademy.heukbaekbookshop.memberset.address.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressRequest;
import com.nhnacademy.heukbaekbookshop.memberset.address.dto.MemberAddressResponse;
import com.nhnacademy.heukbaekbookshop.memberset.address.service.MemberAddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberAddressController.class)
class MemberAddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberAddressService memberAddressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMemberAddressWithValidData() throws Exception {
        // 유효한 MemberAddressRequest 데이터 생성
        MemberAddressRequest validRequest = new MemberAddressRequest(
                12345L,
                "123 Main St",
                "Apt 101",
                "집"
        );

        // Mock MemberAddressResponse 데이터 생성
        MemberAddressResponse mockResponse = new MemberAddressResponse(
                1L,
                12345L,
                "123 Main St",
                "Apt 101",
                "집"
        );

        // Mock 서비스 동작 설정
        when(memberAddressService.createMemberAddress(anyLong(), any(MemberAddressRequest.class)))
                .thenReturn(mockResponse);

        // 테스트 수행
        mockMvc.perform(post("/api/members/addresses")
                        .header(MemberAddressController.X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.addressId").value(1L))
                .andExpect(jsonPath("$.postalCode").value(12345L))
                .andExpect(jsonPath("$.roadNameAddress").value("123 Main St"))
                .andExpect(jsonPath("$.detailAddress").value("Apt 101"))
                .andExpect(jsonPath("$.alias").value("집"));
    }

    @Test
    void testCreateMemberAddressWithValidationErrors() throws Exception {
        // Validation 오류가 있는 MemberAddressRequest 데이터 생성
        MemberAddressRequest invalidRequest = new MemberAddressRequest(
                null, // postalCode가 null
                "", // roadNameAddress가 빈 문자열
                "Apt 101",
                "@" // alias가 잘못된 형식
        );

        // 테스트 수행
        mockMvc.perform(post("/api/members/addresses")
                        .header(MemberAddressController.X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetMemberAddress() throws Exception {
        // Mock MemberAddressResponse 데이터 생성
        MemberAddressResponse mockResponse = new MemberAddressResponse(
                1L,
                12345L,
                "123 Main St",
                "Apt 101",
                "집"
        );

        // Mock 서비스 동작 설정
        when(memberAddressService.getMemberAddress(anyLong())).thenReturn(mockResponse);

        // 테스트 수행
        mockMvc.perform(get("/api/members/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressId").value(1L))
                .andExpect(jsonPath("$.postalCode").value(12345L))
                .andExpect(jsonPath("$.roadNameAddress").value("123 Main St"))
                .andExpect(jsonPath("$.detailAddress").value("Apt 101"))
                .andExpect(jsonPath("$.alias").value("집"));
    }

    @Test
    void testGetAllMemberAddresses() throws Exception {
        // Mock MemberAddressResponse 데이터 생성
        List<MemberAddressResponse> mockResponse = List.of(
                new MemberAddressResponse(1L, 12345L, "123 Main St", "Apt 101", "집")
        );

        // Mock 서비스 동작 설정
        when(memberAddressService.getMemberAddressesList(anyLong())).thenReturn(mockResponse);

        // 테스트 수행
        mockMvc.perform(get("/api/members/addresses")
                        .header(MemberAddressController.X_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].addressId").value(1L))
                .andExpect(jsonPath("$[0].postalCode").value(12345L))
                .andExpect(jsonPath("$[0].roadNameAddress").value("123 Main St"))
                .andExpect(jsonPath("$[0].detailAddress").value("Apt 101"))
                .andExpect(jsonPath("$[0].alias").value("집"));
    }

    @Test
    void testUpdateMemberAddressWithValidData() throws Exception {
        // 유효한 MemberAddressRequest 데이터 생성
        MemberAddressRequest validRequest = new MemberAddressRequest(
                12345L,
                "123 Main St",
                "Apt 101",
                "집"
        );

        // Mock MemberAddressResponse 데이터 생성
        MemberAddressResponse mockResponse = new MemberAddressResponse(
                1L,
                12345L,
                "123 Main St",
                "Apt 101",
                "집"
        );

        // Mock 서비스 동작 설정
        when(memberAddressService.updateMemberAddress(anyLong(), any(MemberAddressRequest.class)))
                .thenReturn(mockResponse);

        // 테스트 수행
        mockMvc.perform(put("/api/members/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressId").value(1L))
                .andExpect(jsonPath("$.postalCode").value(12345L))
                .andExpect(jsonPath("$.roadNameAddress").value("123 Main St"))
                .andExpect(jsonPath("$.detailAddress").value("Apt 101"))
                .andExpect(jsonPath("$.alias").value("집"));
    }

    @Test
    void testUpdateMemberAddressWithValidationErrors() throws Exception {
        // Validation 오류가 있는 MemberAddressRequest 데이터 생성
        MemberAddressRequest invalidRequest = new MemberAddressRequest(
                null, // postalCode가 null
                "", // roadNameAddress가 빈 문자열
                "Apt 101",
                "@" // alias가 잘못된 형식
        );

        // 테스트 수행
        mockMvc.perform(put("/api/members/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteMemberAddress() throws Exception {
        // Mock 서비스 동작 설정
        doNothing().when(memberAddressService).deleteMemberAddress(anyLong());

        // 테스트 수행
        mockMvc.perform(delete("/api/members/addresses/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCountMemberAddresses() throws Exception {
        // Mock 서비스 동작 설정
        when(memberAddressService.countByMemberId(anyLong())).thenReturn(5L);

        // 테스트 수행
        mockMvc.perform(get("/api/members/addresses/count")
                        .header(MemberAddressController.X_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5L));
    }

}