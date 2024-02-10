package com.psj.itembrowser.order.domain.vo;

import java.util.Objects;

import lombok.Getter;
import lombok.NonNull;

@Getter
public enum OrderStatus {
	PENDING("PENDING"), // 주문 대기중

	ACCEPT("ACCEPT"), // 주문 접수됨

	INSTRUCT("INSTRUCT"), // 	상품 준비중

	DEPARTURE("DEPARTURE"), // 배송 지시

	DELIVERING("DELIVERING"), // 배송중

	FINAL_DELIVERY("FINAL_DELIVERY"), // 배송 완료

	NONE_TRACKING("NONE_TRACKING"), // 업체 직접 배송

	CANCELED("CANCELED"), // 주문 취소됨
	;

	private final String name;
	private final String value;

	OrderStatus(@NonNull String value) {
		this.value = value;
		this.name = name();
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