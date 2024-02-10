package com.psj.itembrowser.cart.service;

import org.springframework.stereotype.Service;

import com.psj.itembrowser.cart.domain.dto.request.CartProductDeleteRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartProductRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartProductUpdateRequestDTO;
import com.psj.itembrowser.cart.domain.dto.response.CartResponseDTO;

/**
 * packageName    : com.psj.itembrowser.test.service
 * fileName       : TestService
 * author         : ipeac
 * date           : 2023-09-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-27        ipeac       최초 생성
 */
@Service
public interface CartService {
	CartResponseDTO getCart(String userId);

	CartResponseDTO getCart(Long cartId);

	void addCart(String userId);

	void addCartProduct(CartProductRequestDTO cartProductRequestDTO);

	void modifyCartProduct(CartProductUpdateRequestDTO cartProductUpdateRequestDTO);

	void removeCart(CartProductDeleteRequestDTO cartProductDeleteRequestDTO);
}