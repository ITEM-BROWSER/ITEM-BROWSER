package com.psj.itembrowser.cart.domain.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartProductUpdateRequestDTO {
    @NotNull
    Long cartId;
    
    @NotNull
    Long productId;
    
    @PositiveOrZero
    long quantity;
}