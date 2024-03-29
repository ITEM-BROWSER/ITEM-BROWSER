package com.psj.itembrowser.shippingInfos.domain.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for {@link ShippingInfo}
 */
@Data
@Builder
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
}