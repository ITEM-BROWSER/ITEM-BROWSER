package com.psj.itembrowser.cart.domain.dto.response;

import com.psj.itembrowser.cart.domain.vo.Cart;
import com.psj.itembrowser.product.domain.dto.response.ProductResponseDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO for {@link Cart}
 */
@Data
@Builder
@EqualsAndHashCode(of = {"userId"})
@AllArgsConstructor
public class CartResponseDTO {
    
    String userId;
    
    LocalDateTime createdDate;
    
    LocalDateTime updatedDate;
    
    List<CartProductRelationResponseDTO> products;
    
    public static CartResponseDTO of(Cart cart) {
        if (cart == null) {
            return null;
        }
        
        return CartResponseDTO.builder()
            .userId(cart.getUserId())
            .createdDate(cart.getCreatedDate())
            .updatedDate(cart.getUpdatedDate())
            .products(cart.getCartProductRelations()
                .stream()
                .map(cartProductRelation -> CartProductRelationResponseDTO.of(
                    cartProductRelation.getCartId(),
                    cartProductRelation.getProductId(),
                    cartProductRelation.getProductQuantity(),
                    ProductResponseDTO.of(cartProductRelation.getProduct())
                )).collect(Collectors.toList()))
            
            .build();
    }
}