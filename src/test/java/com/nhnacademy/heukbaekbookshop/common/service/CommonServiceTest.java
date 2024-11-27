//package com.nhnacademy.heukbaekbookshop.common.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class CommonServiceTest {
//
//    private CommonService commonService;
//
//    @BeforeEach
//    void setUp() {
//        commonService = new CommonService();
//    }
//
//    @Test
//    void testGetSalePrice() {
//        // Given
//        BigDecimal price = BigDecimal.valueOf(10000); // 원래 가격
//        BigDecimal discountRate = BigDecimal.valueOf(20); // 할인율 20%
//
//        // When
//        BigDecimal result = commonService.getSalePrice(price, discountRate);
//
//        // Then
//        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(8000)); // 할인된 가격 8000원 확인
//    }
//
//    @Test
//    void testGetSalePrice_NoDiscount() {
//        // Given
//        BigDecimal price = BigDecimal.valueOf(50000); // 원래 가격
//        BigDecimal discountRate = BigDecimal.ZERO; // 할인율 0%
//
//        // When
//        BigDecimal result = commonService.getSalePrice(price, discountRate);
//
//        // Then
//        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(50000)); // 할인 없음, 원래 가격 그대로 확인
//    }
//
//    @Test
//    void testGetSalePrice_FullDiscount() {
//        // Given
//        BigDecimal price = BigDecimal.valueOf(30000); // 원래 가격
//        BigDecimal discountRate = BigDecimal.valueOf(100); // 할인율 100%
//
//        // When
//        BigDecimal result = commonService.getSalePrice(price, discountRate);
//
//        // Then
//        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO); // 할인 100%, 최종 가격 0원 확인
//    }
//
//    @Test
//    void testFormatPrice() {
//        // Given
//        BigDecimal price = BigDecimal.valueOf(12345678);
//
//        // When
//        String formattedPrice = commonService.formatPrice(price);
//
//        // Then
//        assertThat(formattedPrice).isEqualTo("12,345,678"); // 한국 로케일 포맷 확인
//    }
//
//    @Test
//    void testFormatDate() {
//        // Given
//        Date date = new Date();
//        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy년 MM월");
//        String expectedDate = expectedFormat.format(date);
//
//        // When
//        String formattedDate = commonService.formatDate(date);
//
//        // Then
//        assertThat(formattedDate).isEqualTo(expectedDate); // 날짜 포맷 확인
//    }
//
//    @Test
//    void testConvertStringToBigDecimal() {
//        // Given
//        String amount = "12,345,678";
//
//        // When
//        BigDecimal result = commonService.convertStringToBigDecimal(amount);
//
//        // Then
//        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(12345678)); // 콤마 제거 후 BigDecimal 변환 확인
//    }
//}
