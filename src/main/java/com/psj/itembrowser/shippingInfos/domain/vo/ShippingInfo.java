package com.psj.itembrowser.shippingInfos.domain.vo;

import java.time.LocalDateTime;

import com.psj.itembrowser.shippingInfos.domain.dto.response.ShippingInfoResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
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

	public ShippingInfoResponseDTO toShippingInfoRespDTO() {
		return ShippingInfoResponseDTO
			.builder()
			.id(this.id)
			.userId(this.userId)
			.receiver(this.receiver)
			.mainAddress(this.mainAddress)
			.subAddress(this.subAddress)
			.phoneNumber(this.phoneNumber)
			.alternativeNumber(this.alternativeNumber)
			.shippingRequestMsg(this.shippingRequestMsg)
			.createdDate(this.createdDate)
			.updatedDate(this.updatedDate)
			.build();
	}
}