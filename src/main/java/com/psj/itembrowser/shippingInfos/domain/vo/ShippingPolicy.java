package com.psj.itembrowser.shippingInfos.domain.vo;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *packageName    : com.psj.itembrowser.shippingInfos.domain.vo
 * fileName       : ShippingPolicy
 * author         : ipeac
 * date           : 2024-02-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-13        ipeac       최초 생성
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShippingPolicy implements Serializable {
	/**
	 * default delivery fee
	 */
	private Integer deliveryDefaultFee;
	/**
	 * free shipping over amount
	 */
	private Integer freeShipOverAmount;

	public double calculateShippingFee(double totalPrice) {
		if (totalPrice >= freeShipOverAmount) {
			return 0;
		}
		return deliveryDefaultFee;
	}
}