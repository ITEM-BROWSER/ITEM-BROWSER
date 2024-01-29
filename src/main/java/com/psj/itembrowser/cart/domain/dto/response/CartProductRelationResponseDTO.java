package com.psj.itembrowser.cart.domain.dto.response;

import com.psj.itembrowser.product.domain.dto.response.ProductResponseDTO;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.psj.itembrowser.cart.domain.dto.response fileName       :
 * CartProductRelationResponseDTO author         : ipeac date           : 2023-10-23 description :
 * =========================================================== DATE              AUTHOR NOTE
 * ----------------------------------------------------------- 2023-10-23        ipeac 최초 생성
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartProductRelationResponseDTO {
    
    Long cartId;
    
    Long productId;
    
    @PositiveOrZero
    Long productQuantity;
    
    ProductResponseDTO product;
    
    public static CartProductRelationResponseDTO of(Long cartId, Long productId,
        Long productQuantity, ProductResponseDTO product) {
        return new CartProductRelationResponseDTO(cartId, productId, productQuantity, product);
    }
}