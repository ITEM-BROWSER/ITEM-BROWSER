package com.psj.itembrowser.cart.domain.dto.response;

import com.psj.itembrowser.cart.domain.vo.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

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
}