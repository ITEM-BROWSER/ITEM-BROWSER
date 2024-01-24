package com.psj.itembrowser.order.domain.vo;


import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;

@Getter
public enum OrderStatus {
    ACCEPT("ACCEPT"),
    
    INSTRUCT("INSTRUCT"),
    
    DEPARTURE("DEPARTURE"),
    
    DELIVERING("DELIVERING"),
    
    FINAL_DELIVERY("FINAL_DELIVERY"),
    
    NONE_TRACKING("NONE_TRACKING"),
    
    CANCELED("CANCELED"),
    ;
    
    private final String value;
    
    OrderStatus(@NonNull String value) {
        this.value = value;
    }
    
    public static OrderStatus of(@NonNull String value) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (Objects.equals(
                orderStatus.getValue(), value)) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("주문 상태가 존재하지 않습니다.");
    }
}