package com.psj.itembrowser.cart.domain.dto.response;

import com.psj.itembrowser.cart.domain.vo.Cart;
import com.psj.itembrowser.cart.domain.vo.CartProductRelationMockDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link Cart}
 */
@Data
@Builder
@EqualsAndHashCode(of = {"userId"})
@AllArgsConstructor
public class CartSimpleResponseDTO implements Serializable {
    String userId;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
    List<CartProductRelationMockDTO> products;
    
    public CartResponseDTO toCartResponseDTO() {
        return new CartResponseDTO(
                this.userId,
                this.createdDate,
                this.updatedDate,
                this.products
                        .stream()
                        .map(CartProductRelationMockDTO::toCartProductRelationResponseDTO)
                        .collect(Collectors.toList())
        );
    }
}