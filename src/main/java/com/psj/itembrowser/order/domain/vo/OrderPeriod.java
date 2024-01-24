package com.psj.itembrowser.order.domain.vo;

import java.util.Objects;
import lombok.Getter;

/**
 * packageName    : com.psj.itembrowser.order.domain.vo fileName       : OrderPeriod author : ipeac
 * date           : 2024-01-24 description    :
 * =========================================================== DATE              AUTHOR NOTE
 * ----------------------------------------------------------- 2024-01-24        ipeac       최초 생성
 */
@Getter
public enum OrderPeriod {
    SIX_MONTH(6),
    ONE_YEAR(12),
    TWO_YEAR(24),
    THREE_YEAR(36),
    ;
    private final int month;
    
    OrderPeriod(int month) {
        this.month = month;
    }
    
    public static OrderPeriod of(String period) {
        for (OrderPeriod value : values()) {
            if (Objects.equals(value.name(), period)) {
                return value;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 주문 기간입니다.");
    }
}