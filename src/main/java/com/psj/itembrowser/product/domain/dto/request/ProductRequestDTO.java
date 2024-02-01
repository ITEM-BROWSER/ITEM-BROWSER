package com.psj.itembrowser.product.domain.dto.request;

import com.psj.itembrowser.product.domain.vo.DeliveryFeeType;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.domain.vo.ProductImage;
import com.psj.itembrowser.product.domain.vo.ProductStatus;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class ProductRequestDTO {

    @NotBlank
    @Length(min = 1, max = 100, message = "The product name must be at least 1 character and less than 100 characters.")
    private String name;

    private Integer category;

    @NotBlank
    @Length(min = 10, max = 1000, message = "The product detail must be at least 10 character and less than 1000 characters.")
    private String detail;

    private ProductStatus status;

    @Min(value = 0, message = "Quantity must be greater than 0.")
    private Integer quantity;

    @Min(value = 0, message = "Price must be greater than 0.")
    private Integer unitPrice;

    private String sellerId;

    @NotNull(message = "Sell start date must not be null.")
    @FutureOrPresent(message = "The sales start date must be present or in the future")
    private LocalDateTime sellStartDatetime;

    @NotNull(message = "Sell sell end date must not be null.")
    @FutureOrPresent(message = "The sales end date must be present or in the future")
    private LocalDateTime sellEndDatetime;

    private String displayName;

    private String brand;

    private DeliveryFeeType deliveryFeeType;

    private String deliveryMethod;

    private Integer deliveryDefaultFee;

    private Integer freeShipOverAmount;

    private String returnCenterCode;

    @Size(min = 3, max = 10, message = "Images must be at least 3 and not more than 10.")
    @Valid
    private List<ProductImage> productImages;

    public Product toProduct() {
        return Product.builder().name(this.name)
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
            .productImages(this.productImages)
            .build();
    }

}
