package com.psj.itembrowser.product.domain.dto.response;

import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.domain.vo.ProductImage;
import com.psj.itembrowser.product.domain.vo.ProductStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDTO {
    
    Long id;
    
    String name;
    
    ProductStatus status;
    
    Integer quantity;
    
    LocalDateTime sellStartDatetime;
    
    LocalDateTime sellEndDatetime;
    
    String displayName;
    
    String brand;
    
    String deliveryMethod;
    
    Integer deliveryDefaultFee;

    List<ProductImage> productImages;
    
    public static ProductResponseDTO of(Product product) {
        if (product == null) {
            return null;
        }
        
        return ProductResponseDTO.builder()
            .id(product.getId())
            .name(product.getName())
            .status(product.getStatus())
            .quantity(product.getQuantity())
            .sellStartDatetime(product.getSellStartDatetime())
            .sellEndDatetime(product.getSellEndDatetime())
            .displayName(product.getDisplayName())
            .brand(product.getBrand())
            .deliveryMethod(product.getDeliveryMethod())
            .deliveryDefaultFee(product.getDeliveryDefaultFee())
            .productImages(product.getProductImages())
            .build();
    }
    
}