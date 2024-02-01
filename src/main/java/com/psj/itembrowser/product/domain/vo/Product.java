package com.psj.itembrowser.product.domain.vo;

import static com.psj.itembrowser.common.exception.ErrorCode.PRODUCT_VALIDATION_FAIL;

import com.psj.itembrowser.cart.domain.vo.CartProductRelation;
import com.psj.itembrowser.common.BaseDateTimeEntity;
import com.psj.itembrowser.common.exception.BadRequestException;
import com.psj.itembrowser.product.domain.dto.request.ProductQuantityUpdateRequestDTO;
import com.psj.itembrowser.product.domain.dto.response.ProductResponseDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Product extends BaseDateTimeEntity {

    /**
     * pk값
     */
    private Long id;

    /**
     * 상품명
     */
    private String name;

    /**
     * 상품카테고리. CATEGORY 테이블 참조
     */
    private Integer category;

    /**
     * 상품설명
     */
    private String detail;

    /**
     * 상품상태. 심사중/임시저장/승인대기/승인완료/부분승인/완료/승인반려/상품삭제
     */
    private ProductStatus status;

    /**
     * 재고
     */
    private Integer quantity;

    /**
     * 가격
     */
    private Integer unitPrice;

    /**
     * 판매자ID
     */
    private String sellerId;

    /**
     * 판매시작일시
     */
    private LocalDateTime sellStartDatetime;

    /**
     * 판매종료일시
     */
    private LocalDateTime sellEndDatetime;

    /**
     * 노출상품명. 실제노출되는 상품명
     */
    private String displayName;

    /**
     * 브랜드
     */
    private String brand;

    /**
     * 배송비종류. DELIVERY_FEE_TYPE 테이블 참조
     */
    private DeliveryFeeType deliveryFeeType;

    /**
     * 배송방법. DELIVERY_METHOD 테이블 참조
     */
    private String deliveryMethod;

    /**
     * 기본배송비. 기본 배송
     */
    private Integer deliveryDefaultFee;

    /**
     * 무료배송금액. 무료 배송 기준 금액
     */
    private Integer freeShipOverAmount;

    /**
     * 반품지 센터 코드. CENTER 테이블 참조
     */
    private String returnCenterCode;

    private List<CartProductRelation> cartProductRelations;

    private List<ProductImage> productImages;

    public void validateSellDates() {
        if (this.sellStartDatetime.isBefore(this.sellEndDatetime)) {
            throw new BadRequestException(PRODUCT_VALIDATION_FAIL);
        }
    }

    // 상품 재고를 줄이는 메서드
    public void decreaseStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity can not be less than 0");
        }
        int restStock = this.quantity - quantity;
        if (restStock < 0) {
            throw new IllegalStateException("need more stock");
        }
        this.quantity = restStock;
    }

    // 상품 재고를 늘리는 메서드
    public void increaseStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity can not be less than 0");
        }
        this.quantity += quantity;
    }

    // 상품 재고가 충분한지 확인하는 메서드
    public boolean isEnoughStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity can not be less than 0");
        }
        return this.quantity >= quantity;
    }

    public ProductResponseDTO toProductResponseDTO() {
        return ProductResponseDTO
            .builder()
            .id(this.id)
            .name(this.name)
            .status(this.status)
            .sellStartDatetime(this.sellStartDatetime)
            .sellEndDatetime(this.sellEndDatetime)
            .displayName(this.displayName)
            .brand(this.brand)
            .deliveryMethod(this.deliveryMethod)
            .deliveryDefaultFee(this.deliveryDefaultFee)
            .build();
    }

    public ProductQuantityUpdateRequestDTO toProductQuantityUpdateRequestDTO() {
        return ProductQuantityUpdateRequestDTO
            .builder()
            .id(this.id)
            .quantity(this.quantity)
            .build();
    }
}