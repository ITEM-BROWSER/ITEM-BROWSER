package com.psj.itembrowser.cart.domain.vo;

import com.psj.itembrowser.cart.domain.dto.request.CartProductUpdateRequestDTO;
import com.psj.itembrowser.cart.domain.dto.response.CartProductRelationResponseDTO;
import com.psj.itembrowser.common.BaseDateTimeEntity;
import com.psj.itembrowser.common.exception.DatabaseOperationException;
import com.psj.itembrowser.product.domain.vo.Product;
import lombok.*;

import java.time.LocalDateTime;

import static com.psj.itembrowser.common.exception.ErrorCode.CART_PRODUCT_QUANTITY_NOT_POSITIVE;

/**
 * packageName    : com.psj.itembrowser.cart.domain.vo
 * fileName       : CartProductRelation
 * author         : ipeac
 * date           : 2023-10-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-22        ipeac       최초 생성
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"cartId", "productId"}, callSuper = false)
@ToString
public class CartProductRelation extends BaseDateTimeEntity {
    private Long cartId;
    private Long productId;
    private Long productQuantity;
    
    private Cart cart;
    private Product product;
    
    public CartProductRelation(
            Long cartId,
            Long productId,
            Long productQuantity,
            Cart cart,
            Product product,
            LocalDateTime createdDate,
            LocalDateTime updatedDate,
            LocalDateTime deletedDate
    ) {
        super(createdDate, updatedDate, deletedDate);
        this.cartId = cartId;
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.cart = cart;
        this.product = product;
    }
    
    public void addProductQuantity(long quantity) {
        if (quantity < 0) {
            throw new DatabaseOperationException(CART_PRODUCT_QUANTITY_NOT_POSITIVE);
        }
        this.productQuantity += quantity;
    }
    
    public CartProductRelationResponseDTO toCartProductRelationResponseDTO() {
        return new CartProductRelationResponseDTO(
                this.cartId,
                this.productId,
                this.productQuantity,
                this.cart.toCartResponseDTO(),
                this.product.toProductResponseDTO()
        );
    }
    
    public CartProductUpdateRequestDTO toCartProductUpdateRequestDTO() {
        return new CartProductUpdateRequestDTO(
                this.cartId,
                this.productId,
                this.productQuantity
        );
    }
    
    public CartProductRelationMockDTO toCartProductRelationSimpleDTO() {
        return new CartProductRelationMockDTO(
                this.cartId,
                this.productId,
                this.productQuantity
        );
    }
}