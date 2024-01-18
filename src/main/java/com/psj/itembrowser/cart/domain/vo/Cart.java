package com.psj.itembrowser.cart.domain.vo;

import com.psj.itembrowser.cart.domain.dto.request.CartRequestDTO;
import com.psj.itembrowser.cart.domain.dto.response.CartResponseDTO;
import com.psj.itembrowser.cart.domain.dto.response.CartSimpleResponseDTO;
import com.psj.itembrowser.common.BaseDateTimeEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@EqualsAndHashCode(of = {"id", "userId"}, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseDateTimeEntity {
    /**
     * pk값
     */
    Long id;
    
    /**
     * 유저ID
     */
    String userId;
    
    List<CartProductRelation> cartProductRelations;
    
    public Cart(LocalDateTime createdDate, LocalDateTime updatedDate, LocalDateTime deletedDate, Long id, String userId, Long productId, int quantity, List<CartProductRelation> cartProductRelations) {
        super(createdDate, updatedDate, deletedDate);
        this.id = id;
        this.userId = userId;
        this.cartProductRelations = cartProductRelations;
    }
    
    // CART -> CartRespDTO
    public CartResponseDTO toCartResponseDTO() {
        return new CartResponseDTO(
                this.userId,
                this.createdDate,
                this.updatedDate,
                this.cartProductRelations
                        .stream()
                        .map(CartProductRelation::toCartProductRelationResponseDTO)
                        .collect(Collectors.toList())
        );
    }
    
    public CartSimpleResponseDTO toCartSimpleResponseDTO() {
        return new CartSimpleResponseDTO(
                this.userId,
                this.createdDate,
                this.updatedDate,
                this.cartProductRelations
                        .stream()
                        .map(CartProductRelation::toCartProductRelationSimpleDTO)
                        .collect(Collectors.toList())
        );
    }
    
    public CartRequestDTO toCartRequestDTO() {
        
        return new CartRequestDTO(this.userId);
    }
}