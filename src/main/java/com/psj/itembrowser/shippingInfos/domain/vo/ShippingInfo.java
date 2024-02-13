package com.psj.itembrowser.shippingInfos.domain.vo;

import java.time.LocalDateTime;

import com.psj.itembrowser.shippingInfos.domain.dto.response.ShippingInfoResponseDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingInfo {
	/**
	 * 배송지 PK
	 */
	private Long id;

	/**
	 * 유저ID
	 */
	private String userId;

	/**
	 * 수취인
	 */
	private String receiver;

	/**
	 * 메인주소
	 */
	private String mainAddress;

	/**
	 * 상세주소
	 */
	private String subAddress;

	/**
	 * 우편번호
	 */
	private String zipCode;

	/**
	 * 휴대폰 번호. 휴대폰번호 (-) 가 없어야함
	 */
	private String phoneNumber;

	/**
	 * 대안 연락처. 번호 (-) 가 없어야함
	 */
	private String alternativeNumber;

	/**
	 * 배송요청사항
	 */
	private String shippingRequestMsg;

	/**
	 * 생성일
	 */
	private LocalDateTime createdDate;

	/**
	 * 업데이트일
	 */
	private LocalDateTime updatedDate;

	/**
	 * 삭제일
	 */
	private LocalDateTime deletedDate;

	public static ShippingInfo from(ShippingInfoResponseDTO shippingInfoResponseDTO) {
		ShippingInfo shippingInfo = new ShippingInfo();

		shippingInfo.id = shippingInfoResponseDTO.getId();
		shippingInfo.userId = shippingInfoResponseDTO.getUserId();
		shippingInfo.receiver = shippingInfoResponseDTO.getReceiver();
		shippingInfo.mainAddress = shippingInfoResponseDTO.getMainAddress();
		shippingInfo.subAddress = shippingInfoResponseDTO.getSubAddress();
		shippingInfo.phoneNumber = shippingInfoResponseDTO.getPhoneNumber();
		shippingInfo.alternativeNumber = shippingInfoResponseDTO.getAlternativeNumber();
		shippingInfo.shippingRequestMsg = shippingInfoResponseDTO.getShippingRequestMsg();
		shippingInfo.createdDate = shippingInfoResponseDTO.getCreatedDate();
		shippingInfo.updatedDate = shippingInfoResponseDTO.getUpdatedDate();
		shippingInfo.deletedDate = shippingInfoResponseDTO.getDeletedDate();

		return shippingInfo;
	}

	@Override
	public String toString() {
		return "ShippingInfo{" +
			"id=" + id +
			", userId='" + userId + '\'' +
			", receiver='" + receiver + '\'' +
			", mainAddress='" + mainAddress + '\'' +
			", subAddress='" + subAddress + '\'' +
			", phoneNumber='" + phoneNumber + '\'' +
			", alternativeNumber='" + alternativeNumber + '\'' +
			", shippingRequestMsg='" + shippingRequestMsg + '\'' +
			", createdDate=" + createdDate +
			", updatedDate=" + updatedDate +
			", deletedDate=" + deletedDate +
			'}';
	}
}