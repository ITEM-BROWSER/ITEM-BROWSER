package com.psj.itembrowser.order.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import com.psj.itembrowser.security.common.exception.BadRequestException;
import com.psj.itembrowser.security.common.openfeign.dto.AddressResponseDto;
import com.psj.itembrowser.security.common.openfeign.service.AddressApiOpenFeign;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;

@ExtendWith(MockitoExtension.class)
public class ShppingInfoValidationServiceTest {

	@Mock
	private AddressApiOpenFeign addressApiOpenFeign;

	@InjectMocks
	private ShppingInfoValidationService shppingInfoValidationService;

	@Value("${juso.confmKey}")
	private String confmKey;

	private ShippingInfo shippingInfo;

	private AddressResponseDto addressResponseDto;

	@BeforeEach
	public void setUp() {
		shippingInfo = mock(ShippingInfo.class);
		addressResponseDto = mock(AddressResponseDto.class);
	}

	@Test
	@DisplayName("주소가 유효한 경우 유효한 결과를 반환한다.")
	public void When_ValidAddress_Expect_ValidResult() {
		//given
		given(shippingInfo.getZipCode()).willReturn("01111");
		given(addressResponseDto.getCommon()).willReturn(new AddressResponseDto.Common("1", 1, 1, "", ""));
		given(addressApiOpenFeign.getAddress(confmKey, "1", "1", shippingInfo.getZipCode(), "json"))
			.willReturn(addressResponseDto);

		//when
		AddressValidationResult result = shppingInfoValidationService.validateAddress(shippingInfo);

		//then
		assertThat(result.isValid()).isTrue();
		assertThat(result.getAddressResponseDto()).isNotNull();
	}

	@Test
	@DisplayName("주소가 유효하지 않은 경우 BadRequestException을 발생시킨다.")
	public void testValidateAddressWhenAddressNotFoundThenThrowBadRequestException() {
		//given
		given(shippingInfo.getZipCode()).willReturn("01111");
		given(addressResponseDto.getCommon()).willReturn(new AddressResponseDto.Common("0", 1, 1, "", ""));
		given(addressApiOpenFeign.getAddress(confmKey, "1", "1", shippingInfo.getZipCode(), "json"))
			.willReturn(addressResponseDto);

		//when - then
		assertThatThrownBy(() -> shppingInfoValidationService.validateAddress(shippingInfo))
			.isInstanceOf(BadRequestException.class)
			.hasMessage("Not Found Address");
	}
}