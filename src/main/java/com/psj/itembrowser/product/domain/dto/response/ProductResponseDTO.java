package com.psj.itembrowser.product.domain.dto.response;

import com.psj.itembrowser.product.domain.vo.ProductStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

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
}