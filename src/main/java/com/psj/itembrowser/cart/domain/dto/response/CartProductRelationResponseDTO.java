package com.psj.itembrowser.cart.domain.dto.response;

import com.psj.itembrowser.product.domain.dto.response.ProductResponseDTO;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;

/**
 * packageName    : com.psj.itembrowser.cart.domain.dto.response
 * fileName       : CartProductRelationResponseDTO
 * author         : ipeac
 * date           : 2023-10-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-23        ipeac       최초 생성
 */
@Data
@NoArgsConstructor
public class CartProductRelationResponseDTO {
    Long cartId;
    
    Long productId;
    
    @PositiveOrZero
    Long productQuantity;
    
    CartResponseDTO cart;
    
    ProductResponseDTO product;
    
    @Builder
    public CartProductRelationResponseDTO(Long cartId, Long productId, Long productQuantity, CartResponseDTO cart, ProductResponseDTO product) {
        this.cartId = cartId;
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.cart = cart;
        this.product = product;
    }
}