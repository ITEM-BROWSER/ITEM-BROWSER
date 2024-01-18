package com.psj.itembrowser.cart.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartProductRequestDTO {
    Long cartId;
    
    Long productId;
    
    String userId;
    
    @PositiveOrZero
    long quantity;
}