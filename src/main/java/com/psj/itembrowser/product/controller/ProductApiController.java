package com.psj.itembrowser.product.controller;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.psj.itembrowser.product.domain.dto.request.ProductRequestDTO;
import com.psj.itembrowser.product.domain.dto.request.ProductUpdateDTO;
import com.psj.itembrowser.product.service.ProductService;
import com.psj.itembrowser.security.common.message.MessageDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ProductApiController {

	private final ProductService productService;

	@PostMapping(value = "/v1/api/products", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public MessageDTO createProduct(@Valid @ModelAttribute ProductRequestDTO productRequestDTO) {
		productService.createProduct(productRequestDTO);
		return new MessageDTO("Insert Success");
	}

	@PutMapping("/v1/api/products/{productId}")
	public MessageDTO modifyProduct(@Valid @ModelAttribute ProductUpdateDTO productUpdateDTO,
		@PathVariable Long productId) {
		productService.updateProduct(productUpdateDTO, productId);
		return new MessageDTO("Modify Success");
	}
}