package com.psj.itembrowser.shippingInfos.domain.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link ShippingInfo}
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class ShippingInfoResponseDTO implements Serializable {
	Long id;
	String userId;
	String receiver;
	String mainAddress;
	String subAddress;
	String phoneNumber;
	String alternativeNumber;
	String shippingRequestMsg;
	LocalDateTime createdDate;
	LocalDateTime updatedDate;
	LocalDateTime deletedDate;
}