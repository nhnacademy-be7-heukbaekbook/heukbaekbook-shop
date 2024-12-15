package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.nhnacademy.heukbaekbookshop.image.domain.WrappingPaperImage;
import com.nhnacademy.heukbaekbookshop.image.service.ImageManagerService;
import com.nhnacademy.heukbaekbookshop.order.domain.WrappingPaper;
import com.nhnacademy.heukbaekbookshop.order.repository.WrappingPaperImageRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.WrappingPaperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WrappingPaperAdminService {
    private final WrappingPaperRepository wrappingPaperRepository;
    private final WrappingPaperImageRepository wrappingPaperImageRepository;
    private final ImageManagerService imageManagerService;

    // 포장지 등록
    @Transactional
    public WrappingPaper addWrappingPaper(String name, BigDecimal price, MultipartFile imageFile) {
        // 포장지 저장
        WrappingPaper wrappingPaper = new WrappingPaper();
        wrappingPaper.setName(name);
        wrappingPaper.setPrice(price);
        WrappingPaper savedWrappingPaper = wrappingPaperRepository.save(wrappingPaper);

        // 이미지 업로드 및 WrappingPaperImage 저장
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = imageManagerService.uploadPhoto(imageFile, ImageType.WRAPPING_PAPER);
            WrappingPaperImage wrappingPaperImage = new WrappingPaperImage();
            wrappingPaperImage.setWrappingPaper(savedWrappingPaper);
            wrappingPaperImage.setUrl(imageUrl);
            wrappingPaperImage.setType(ImageType.WRAPPING_PAPER);
            wrappingPaperImageRepository.save(wrappingPaperImage);
        }

        return savedWrappingPaper;
    }

    // 포장지 수정
    @Transactional
    public WrappingPaper updateWrappingPaper(Long id, String name, BigDecimal price, MultipartFile imageFile) {
        // 기존 WrappingPaper 가져오기
        WrappingPaper wrappingPaper = wrappingPaperRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Wrapping paper not found"));

        // WrappingPaper 필드 업데이트
        wrappingPaper.setName(name);
        wrappingPaper.setPrice(price);
        WrappingPaper updatedWrappingPaper = wrappingPaperRepository.save(wrappingPaper);

        // WrappingPaperImage 업데이트
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = imageManagerService.uploadPhoto(imageFile, ImageType.WRAPPING_PAPER);

            WrappingPaperImage wrappingPaperImage = wrappingPaperImageRepository.findByWrappingPaper(wrappingPaper)
                    .orElse(new WrappingPaperImage()); // 기존 이미지 없으면 새로 생성
            wrappingPaperImage.setWrappingPaper(updatedWrappingPaper);
            wrappingPaperImage.setUrl(imageUrl);
            wrappingPaperImage.setType(ImageType.WRAPPING_PAPER);

            wrappingPaperImageRepository.save(wrappingPaperImage);
        }

        return updatedWrappingPaper;
    }

    // 포장지 삭제
    @Transactional
    public void deleteWrappingPaper(Long id) {
        WrappingPaper wrappingPaper = wrappingPaperRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Wrapping paper not found"));

        // WrappingPaperImage 삭제
        wrappingPaperImageRepository.deleteByWrappingPaper(wrappingPaper);

        // WrappingPaper 삭제
        wrappingPaperRepository.deleteById(id);
    }
}
