package com.psj.itembrowser.order.service.impl;

import com.psj.itembrowser.security.common.openfeign.dto.AddressResponseDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *packageName    : com.psj.itembrowser.order.service.impl
 * fileName       : AddressValidationResult
 * author         : ipeac
 * date           : 2024-02-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-13        ipeac       최초 생성
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AddressValidationResult {
	private boolean isValid;
	private AddressResponseDto addressResponseDto;

	public static AddressValidationResult from(AddressResponseDto addressResponseDto, boolean isValid) {
		return new AddressValidationResult(isValid, addressResponseDto);
	}
}