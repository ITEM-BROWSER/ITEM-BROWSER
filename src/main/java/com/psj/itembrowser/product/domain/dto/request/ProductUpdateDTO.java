package com.psj.itembrowser.product.domain.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.psj.itembrowser.product.domain.vo.DeliveryFeeType;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.domain.vo.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateDTO {

	private Long id;

	@Length(max = 100, message = "The product name must be less than 100 characters.")
	private String name;

	private Integer category;

	@Length(max = 1000, message = "The product detail must be less than 1000 characters.")
	private String detail;

	private ProductStatus status;

	@Min(value = 0, message = "Quantity must be greater than 0.")
	private Integer quantity;

	@Min(value = 0, message = "Price must be greater than 0.")
	private Integer unitPrice;

	private String sellerId;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime sellStartDatetime;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime sellEndDatetime;

	private String displayName;

	private String brand;

	private DeliveryFeeType deliveryFeeType;

	private String deliveryMethod;

	private Integer deliveryDefaultFee;

	private Integer freeShipOverAmount;

	private String returnCenterCode;

	// New image files
	private List<MultipartFile> multipartFiles;

	// List of image IDs to delete
	private List<Long> deleteImageIds;

	public Product toProduct(Long productId) {
		return Product.builder().id(productId)
			.name(this.name)
			.category(this.category)
			.detail(this.detail)
			.status(this.status)
			.quantity(this.quantity)
			.unitPrice(this.unitPrice)
			.sellerId(this.sellerId)
			.sellStartDatetime(this.sellStartDatetime)
			.sellEndDatetime(this.sellEndDatetime)
			.displayName(this.displayName)
			.brand(this.brand)
			.deliveryFeeType(this.deliveryFeeType)
			.deliveryMethod(this.deliveryMethod)
			.deliveryDefaultFee(this.deliveryDefaultFee)
			.freeShipOverAmount(this.freeShipOverAmount)
			.returnCenterCode(this.returnCenterCode)
			.build();
	}
}