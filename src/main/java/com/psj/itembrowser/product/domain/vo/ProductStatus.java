package com.psj.itembrowser.product.domain.vo;

import lombok.Getter;

/**
 * packageName    : com.psj.itembrowser.product.domain.vo
 * fileName       : ProductStatus
 * author         : ipeac
 * date           : 2023-10-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-22        ipeac       최초 생성
 */
@Getter
public enum ProductStatus {
	// 심사중/임시저장/승인대기/승인완료/부분승인/완료/승인반려/상품삭제/품절
	UNDER_REVIEW("underReview"),
	TEMPORARY_SAVE("temporarySave"),
	WAITING_FOR_APPROVAL("waitingForApproval"),
	APPROVED("approved"),
	PARTIALLY_APPROVED("partiallyApproved"),
	COMPLETED("completed"),
	APPROVAL_REJECTED("approvalRejected"),
	PRODUCT_DELETED("productDeleted"),
	SOLD_OUT("soldOut"),
	;

	private final String status;

	ProductStatus(String status) {
		this.status = status;
	}
}