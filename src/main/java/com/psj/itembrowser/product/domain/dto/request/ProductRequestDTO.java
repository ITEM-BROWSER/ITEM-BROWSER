package com.psj.itembrowser.product.domain.dto.request;

import com.psj.itembrowser.product.domain.vo.ProductImage;
import com.psj.itembrowser.product.domain.vo.ProductStatus;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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

    @FutureOrPresent(message = "The sales start date must be present or in the future")
    private LocalDateTime sellStartDatetime;

    @FutureOrPresent(message = "The sales end date must be present or in the future")
    private LocalDateTime sellEndDatetime;

    private String displayName;

    private String brand;

    private String deliveryFeeType;

    private String deliveryMethod;

    private Integer deliveryDefaultFee;

    private Integer freeShipOverAmount;

    private String returnCenterCode;

    @Size(min = 3, max = 10, message = "Images must be at least 3 and not more than 10.")
    @Valid
    private List<ProductImage> productImages;
}
