package com.psj.itembrowser.product.domain.vo;

import com.psj.itembrowser.security.common.BaseDateTimeEntity;
import java.nio.file.Path;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductImage extends BaseDateTimeEntity {

    private Long id;

    private Long productId;

    private String fileName;

    private String filePath;

    private String type;

    private Long size;

    public static ProductImage from(MultipartFile file, Long productId, Path savePath) {
            return ProductImage.builder()
                .productId(productId)
                .fileName(savePath.getFileName().toString())
                .filePath(savePath.toString())
                .type(file.getContentType())
                .size(file.getSize())
                .build();
    }
}
