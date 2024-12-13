package com.nhnacademy.heukbaekbookshop.review;

import com.nhnacademy.heukbaekbookshop.review.dto.request.ReviewImageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewImageConverter {

    public static List<MultipartFile> convertReviewImageRequestsToMultipartFiles(List<ReviewImageRequest> imageRequests) {
        return imageRequests.stream()
                .map(ReviewImageConverter::toMultipartFile)
                .collect(Collectors.toList());
    }

    private static MultipartFile toMultipartFile(ReviewImageRequest request) {
        return new Base64MultipartFile(
                request.getBase64Data(),
                request.getFileName(),
                request.getContentType()
        );
    }

    // Custom MultipartFile Implementation
    private static class Base64MultipartFile implements MultipartFile {

        private final byte[] fileContent;
        private final String fileName;
        private final String contentType;

        public Base64MultipartFile(String base64Data, String fileName, String contentType) {
            this.fileContent = Base64.getDecoder().decode(base64Data);
            this.fileName = fileName;
            this.contentType = contentType;
        }

        @Override
        public String getName() {
            return fileName;
        }

        @Override
        public String getOriginalFilename() {
            return fileName;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return fileContent.length == 0;
        }

        @Override
        public long getSize() {
            return fileContent.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return fileContent;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(fileContent);
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
            throw new UnsupportedOperationException("TransferTo not supported in Base64MultipartFile");
        }
    }
}
