package com.psj.itembrowser.shippingInfos.domain.vo;

import java.io.Serializable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.psj.itembrowser.shippingInfos.domain.vo fileName       : ShippingPolicy author         : ipeac date           : 2024-02-13 description    : =========================================================== DATE              AUTHOR             NOTE ----------------------------------------------------------- 2024-02-13        ipeac       최초 생성
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShippingPolicy implements Serializable {
    
    /**
     * default delivery fee
     */
    private DeliveryFee deliveryDefaultFee;
    /**
     * free shipping over amount
     */
    private Integer freeShipOverAmount;
    
    public DeliveryFee calculateShippingFee(double totalPrice) {
        Objects.requireNonNull(deliveryDefaultFee, "deliveryDefaultFee must not be null");
        Objects.requireNonNull(freeShipOverAmount, "freeShipOverAmount must not be null");
        
        if (totalPrice >= freeShipOverAmount) {
            return DeliveryFee.FREE;
        }
        return DeliveryFee.DEFAULT;
    }
    
    @Getter
    public enum DeliveryFee {
        FREE(0), DEFAULT(3000);
        
        private final String name;
        private final int fee;
        
        DeliveryFee(int fee) {
            this.name = name();
            this.fee = fee;
        }
    }
}