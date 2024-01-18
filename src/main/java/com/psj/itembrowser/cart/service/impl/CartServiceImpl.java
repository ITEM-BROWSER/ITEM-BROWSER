package com.psj.itembrowser.cart.service.impl;

import com.psj.itembrowser.cart.domain.dto.request.CartProductDeleteRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartProductRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartProductUpdateRequestDTO;
import com.psj.itembrowser.cart.domain.dto.response.CartResponseDTO;
import com.psj.itembrowser.cart.domain.vo.CartProductRelation;
import com.psj.itembrowser.cart.mapper.CartMapper;
import com.psj.itembrowser.cart.persistance.CartPersistence;
import com.psj.itembrowser.cart.service.CartService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : com.psj.itembrowser.test.service.impl
 * fileName       : TestServiceImpl
 * author         : ipeac
 * date           : 2023-09-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-27        ipeac       최초 생성
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    private final CartPersistence cartPersistence;
    private final CartMapper cartMapper;
    
    @Override
    public CartResponseDTO getCart(String userId) {
        return cartPersistence.getCart(userId);
    }
    
    @Override
    public CartResponseDTO getCart(Long cartId) {
        return cartPersistence.getCart(cartId);
    }
    
    @Override
    public void addCart(@NonNull String userId) {
        cartPersistence.addCart(userId);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void addCartProduct(CartProductRequestDTO requestDTO) {
        CartResponseDTO cart = getCart(requestDTO.getUserId());
        if (cart == null) {
            addCart(requestDTO.getUserId());
        }
        
        CartProductRelation findCartProduct = cartMapper.getCartProductRelation(requestDTO.getCartId(), requestDTO.getProductId());
        
        if (findCartProduct != null) {
            findCartProduct.addProductQuantity(requestDTO.getQuantity());
            cartPersistence.modifyCartProduct(findCartProduct.toCartProductUpdateRequestDTO());
            return;
        }
        cartPersistence.insertCartProduct(requestDTO);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void modifyCartProduct(CartProductUpdateRequestDTO cartProductUpdateRequestDTO) {
        cartPersistence.modifyCartProduct(cartProductUpdateRequestDTO);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void removeCart(@NonNull CartProductDeleteRequestDTO cartProductDeleteRequestDTO) {
        cartPersistence.deleteCart(cartProductDeleteRequestDTO);
    }
}