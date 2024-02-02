package com.psj.itembrowser.common.exception;

import lombok.Getter;

/**
 *packageName    : com.psj.itembrowser.common.exception
 * fileName       : CustomException
 * author         : ipeac
 * date           : 2024-01-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-30        ipeac       최초 생성
 */
@Getter
public class CustomIllegalStateException extends IllegalStateException {
	
	private final ErrorCode errorCode;
	
	public CustomIllegalStateException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}