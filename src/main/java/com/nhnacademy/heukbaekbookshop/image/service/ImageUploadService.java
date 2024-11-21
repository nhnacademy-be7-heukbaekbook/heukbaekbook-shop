package com.nhnacademy.heukbaekbookshop.image.service;

import com.nhnacademy.heukbaekbookshop.common.storage.ContainerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ImageUploadService {
    private final ContainerService containerService;
    private final String containerName = "heukbaekbook";

    public ImageUploadService(ContainerService containerService) {
        this.containerService = containerService;
    }

    public List<String> uploadImages(String tokenId, List<String> base64Images) {
        List<String> uploadedUrls = new ArrayList<>();
        for (String base64Image : base64Images) {
            byte[] imageData = Base64.getDecoder().decode(base64Image);
            String objectName = generateFileName();
            String url = containerService.uploadObject(tokenId, containerName, objectName, imageData);
            uploadedUrls.add(url);
        }
        return uploadedUrls;
    }

    private String generateFileName() {
        return "img_" + System.currentTimeMillis();
    }
}
