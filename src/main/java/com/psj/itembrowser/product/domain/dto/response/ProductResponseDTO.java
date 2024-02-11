package com.psj.itembrowser.product.domain.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.domain.vo.ProductImage;
import com.psj.itembrowser.product.domain.vo.ProductStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDTO {

	private Long id;

	private String name;

	private Integer category;

	private String detail;

	private ProductStatus status;

	private Integer quantity;

	private Integer unitPrice;

	private String sellerId;

	private LocalDateTime sellStartDatetime;

	private LocalDateTime sellEndDatetime;

	private String displayName;

	private String brand;

	private String deliveryMethod;

	private Integer deliveryDefaultFee;

	private Integer freeShipOverAmount;

	private String returnCenterCode;

	private List<ProductImage> productImages;

	public static ProductResponseDTO of(Product product) {
		if (product == null) {
			return null;
		}

		return ProductResponseDTO.builder()
			.id(product.getId())
			.name(product.getName())
			.status(product.getStatus())
			.quantity(product.getQuantity())
			.sellStartDatetime(product.getSellStartDatetime())
			.sellEndDatetime(product.getSellEndDatetime())
			.displayName(product.getDisplayName())
			.brand(product.getBrand())
			.deliveryMethod(product.getDeliveryMethod())
			.deliveryDefaultFee(product.getDeliveryDefaultFee())
			.productImages(product.getProductImages())
			.build();
	}
}