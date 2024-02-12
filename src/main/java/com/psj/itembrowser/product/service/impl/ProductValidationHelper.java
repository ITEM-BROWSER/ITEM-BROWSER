package com.psj.itembrowser.product.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.psj.itembrowser.product.domain.dto.response.ProductResponseDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.service.ProductService;
import com.psj.itembrowser.security.common.exception.BadRequestException;
import com.psj.itembrowser.security.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *packageName    : com.psj.itembrowser.product.service.impl
 * fileName       : ProductValidationHelper
 * author         : ipeac
 * date           : 2024-02-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-12        ipeac       최초 생성
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductValidationHelper {
	private final ProductService productService;

	public void validateProduct(List<Product> products) {
		products.forEach(product -> {
			ProductResponseDTO productResponseDTO = productService.getProduct(product.getId());

			if (product.isEnoughStock(productResponseDTO.getQuantity()) == false) {
				throw new BadRequestException(ErrorCode.PRODUCT_QUANTITY_NOT_ENOUGH);
			}
		});
	}
}