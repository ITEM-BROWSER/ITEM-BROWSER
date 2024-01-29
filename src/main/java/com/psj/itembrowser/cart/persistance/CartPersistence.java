package com.psj.itembrowser.cart.persistance;

import static com.psj.itembrowser.common.exception.ErrorCode.CART_INSERT_FAIL;
import static com.psj.itembrowser.common.exception.ErrorCode.CART_NOT_FOUND;
import static com.psj.itembrowser.common.exception.ErrorCode.CART_PRODUCT_DELETE_FAIL;
import static com.psj.itembrowser.common.exception.ErrorCode.CART_PRODUCT_INSERT_FAIL;
import static com.psj.itembrowser.common.exception.ErrorCode.CART_PRODUCT_NOT_FOUND;
import static com.psj.itembrowser.common.exception.ErrorCode.CART_PRODUCT_UPDATE_FAIL;

import com.psj.itembrowser.cart.domain.dto.request.CartProductDeleteRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartProductRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartProductUpdateRequestDTO;
import com.psj.itembrowser.cart.domain.dto.response.CartResponseDTO;
import com.psj.itembrowser.cart.domain.vo.Cart;
import com.psj.itembrowser.cart.mapper.CartMapper;
import com.psj.itembrowser.common.exception.DatabaseOperationException;
import com.psj.itembrowser.common.exception.NotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.psj.itembrowser.cart.persistance fileName       : CartPersistence author
 *     : ipeac date           : 2023-10-06 description    :
 * =========================================================== DATE              AUTHOR
 * NOTE ----------------------------------------------------------- 2023-10-06        ipeac       최초
 * 생성
 */
@Component
@RequiredArgsConstructor
public class CartPersistence {
    
    private final CartMapper cartMapper;
    
    public CartResponseDTO getCart(@NonNull String userId) {
        Cart cart = cartMapper.getCartByUserId(userId);
        if (cart == null) {
            throw new NotFoundException(CART_NOT_FOUND);
        }
        return CartResponseDTO.of(cart);
    }
    
    public CartResponseDTO getCart(@NonNull Long cartId) {
        Cart cart = cartMapper.getCart(cartId);
        if (cart == null) {
            throw new NotFoundException(CART_PRODUCT_NOT_FOUND);
        }
        return CartResponseDTO.of(cart);
    }
    
    public void insertCartProduct(@NonNull CartProductRequestDTO cartProductRequestDTO) {
        boolean isNotInserted = !cartMapper.insertCartProduct(cartProductRequestDTO);
        if (isNotInserted) {
            throw new DatabaseOperationException(CART_PRODUCT_INSERT_FAIL);
        }
    }
    
    public void modifyCartProduct(
        @NonNull CartProductUpdateRequestDTO cartProductUpdateRequestDTO) {
        boolean isNotModified = !cartMapper.updateCartProductRelation(cartProductUpdateRequestDTO);
        if (isNotModified) {
            throw new DatabaseOperationException(CART_PRODUCT_UPDATE_FAIL);
        }
    }
    
    public void deleteCart(@NonNull CartProductDeleteRequestDTO cartProductDeleteRequestDTO) {
        boolean isNotDeleted = !cartMapper.deleteCartProductRelation(cartProductDeleteRequestDTO);
        if (isNotDeleted) {
            throw new DatabaseOperationException(CART_PRODUCT_DELETE_FAIL);
        }
    }
    
    public void addCart(@NonNull String userId) {
        boolean isNotAdded = !cartMapper.insertCart(userId);
        if (isNotAdded) {
            throw new DatabaseOperationException(CART_INSERT_FAIL);
        }
    }
}