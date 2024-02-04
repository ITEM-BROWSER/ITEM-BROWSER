package com.psj.itembrowser.product.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

@UtilityClass
public class FileUtil {

    private static final Set<String> ALLOWED_IMAGE_MIME_TYPES = new HashSet<>(
        Arrays.asList("image/png", "image/jpg", "image/gif", "image/bmp", "image/jpeg")
    );

    public void validateImageFile(MultipartFile file) {
        validateMimeType(file);
        validateFileName(file.getOriginalFilename());
    }
    public void validateNumberOfImageFiles(List<MultipartFile> files) {
        if (files.size() < 3 || files.size() > 10) {
            throw new IllegalArgumentException("Images must be at least 3 and not more than 10. NowSize : " + files.size());
        }
    }

    private void validateFileName(String fileName) {
        if (fileName == null || fileName.contains("..")) {
            throw new IllegalArgumentException("Filename is invalid.");
        }

        if (!fileName.matches("^[a-zA-Z0-9._가-힣-]+$")) {
            throw new IllegalArgumentException("Filename contains invalid characters.");
        }
    }

    private void validateMimeType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_MIME_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Invalid file type, only image files are allowed.");
        }
    }
}
