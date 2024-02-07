package com.psj.itembrowser.product.domain.vo;

import lombok.Getter;

/**
 * packageName    : com.psj.itembrowser.product.domain.vo
 * fileName       : DeliveryFeeType
 * author         : ipeac
 * date           : 2023-10-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-22        ipeac       최초 생성
 */
@Getter
public enum DeliveryFeeType {
	// ENUM('FREE', 'NOT_FREE', 'CHARGE_RECEIVED', 'CONDITIONAL_FREE')

	FREE("free"),
	NOT_FREE("notFree"),
	CHARGE_RECEIVED("chargeReceived"),
	CONDITIONAL_FREE("conditionalFree"),
	;

	private final String deliveryFeeType;

	DeliveryFeeType(String deliveryFeeType) {
		this.deliveryFeeType = deliveryFeeType;
	}
}