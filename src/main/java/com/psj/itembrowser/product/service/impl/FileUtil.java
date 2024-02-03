package com.psj.itembrowser.product.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

@UtilityClass
public class FileUtil {

    private static final Set<String> ALLOWED_IMAGE_MIME_TYPES = new HashSet<>(
        Arrays.asList("image/png", "image/jpg", "image/gif", "image/bmp", "image/jpeg")
    );

    public void isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_MIME_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Invalid file type, only image files are allowed.");
        }
    }
}
