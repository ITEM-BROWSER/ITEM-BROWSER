package com.psj.itembrowser.product.domain.vo;

import com.psj.itembrowser.common.BaseDateTimeEntity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductImage extends BaseDateTimeEntity{

    private Long id;

    private Long productId;

    private String fileName;

    private String filePath;

    private String type;

    private Long size;

    public static ProductImage from(MultipartFile file, Long productId, String uploadDir) {
        String originalFilename = file.getOriginalFilename();
        String savedName = UUID.randomUUID() + "_" + originalFilename;

        Path uploadPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path savePath = uploadPath.resolve(savedName);
            file.transferTo(savePath);

            return ProductImage.builder()
                .productId(productId)
                .fileName(savedName)
                .filePath(savePath.toString())
                .type(file.getContentType())
                .size(file.getSize())
                .build();

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + originalFilename, e);
        }
    }
}
