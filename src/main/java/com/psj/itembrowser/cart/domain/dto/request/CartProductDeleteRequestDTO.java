package com.psj.itembrowser.cart.domain.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartProductDeleteRequestDTO {
    @NotNull
    Long cartId;
    
    @NotNull
    Long productId;
}