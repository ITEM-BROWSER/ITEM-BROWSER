package com.psj.itembrowser.order.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.psj.itembrowser.security.common.exception.BadRequestException;
import com.psj.itembrowser.security.common.exception.ErrorCode;
import com.psj.itembrowser.security.common.openfeign.dto.AddressResponseDto;
import com.psj.itembrowser.security.common.openfeign.service.AddressApiOpenFeign;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;

import lombok.RequiredArgsConstructor;

/**
 *packageName    : com.psj.itembrowser.order.service.impl
 * fileName       : ShppingInfoValidationService
 * author         : ipeac
 * date           : 2024-02-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-13        ipeac       최초 생성
 */
@Service
@RequiredArgsConstructor
public class ShppingInfoValidationService {
	private final AddressApiOpenFeign addressApiOpenFeign;

	@Value("${juso.confmKey}")
	private String confmKey;

	public AddressValidationResult validateAddress(ShippingInfo shippingInfo) {
		AddressResponseDto addressResponseDto = addressApiOpenFeign.getAddress(confmKey, "1", "1",
			shippingInfo.getZipCode(), "json");

		if (Objects.equals(addressResponseDto.getCommon().getTotalCount(), "0")) {
			throw new BadRequestException(ErrorCode.ADDRESS_NOT_FOUND);
		}

		return AddressValidationResult.from(addressResponseDto, true);
	}

}