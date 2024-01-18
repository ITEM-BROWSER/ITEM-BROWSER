package com.psj.itembrowser.cart.domain.vo;

import com.psj.itembrowser.cart.domain.dto.response.CartProductRelationResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.psj.itembrowser.cart.domain.vo
 * fileName       : CartProductRelationSimpleDTO
 * author         : ipeac
 * date           : 2023-10-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-24        ipeac       최초 생성
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartProductRelationMockDTO {
    Long cartId;
    Long productId;
    Long productQuantity;
    
    public CartProductRelationResponseDTO toCartProductRelationResponseDTO() {
        return new CartProductRelationResponseDTO(
                this.cartId,
                this.productId,
                this.productQuantity,
                null,
                null
        );
    }
}