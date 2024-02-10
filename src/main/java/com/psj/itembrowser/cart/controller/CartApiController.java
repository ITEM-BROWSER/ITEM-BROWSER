package com.psj.itembrowser.cart.controller;

import java.text.MessageFormat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.psj.itembrowser.cart.domain.dto.request.CartProductDeleteRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartProductRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartProductUpdateRequestDTO;
import com.psj.itembrowser.cart.domain.dto.response.CartResponseDTO;
import com.psj.itembrowser.cart.service.CartService;
import com.psj.itembrowser.security.common.message.MessageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * packageName    : com.psj.itembrowser.test.api
 * fileName       : TestApiController
 * author         : ipeac
 * date           : 2023-09-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-27        ipeac       최초 생성
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/api/cart")
public class CartApiController {
	private final CartService cartService;

	@GetMapping("/{userId}/carts")
	public ResponseEntity<CartResponseDTO> getCart(@PathVariable String userId) {
		CartResponseDTO cart = cartService.getCart(userId);

		return ResponseEntity.ok(cart);
	}

	@PostMapping("/add")
	public MessageDTO addCart(@RequestBody CartProductRequestDTO cartProductRequestDTO) {

		cartService.addCartProduct(cartProductRequestDTO);

		return new MessageDTO(MessageFormat.format(
			"cart add affected : {0} / {1}",
			cartProductRequestDTO.getCartId(),
			cartProductRequestDTO.getProductId()
		));
	}

	@PutMapping("/update")
	public MessageDTO modifyCart(@RequestBody CartProductUpdateRequestDTO cartProductUpdateRequestDTO) {
		cartService.modifyCartProduct(cartProductUpdateRequestDTO);

		return new MessageDTO(MessageFormat.format(
			"cart update affected : {0} / {1}",
			cartProductUpdateRequestDTO.getCartId(),
			cartProductUpdateRequestDTO.getProductId()
		));
	}

	@DeleteMapping("/delete")
	public MessageDTO removeCart(@RequestBody CartProductDeleteRequestDTO cartProductDeleteRequestDTO) {
		cartService.removeCart(cartProductDeleteRequestDTO);

		return new MessageDTO(MessageFormat.format(
			"cart delete affected : {0} / {1}",
			cartProductDeleteRequestDTO.getCartId(),
			cartProductDeleteRequestDTO.getProductId()
		));
	}
}