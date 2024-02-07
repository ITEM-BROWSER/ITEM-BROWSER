package com.psj.itembrowser.security.common.exception;

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
public class CustomRuntimeException extends RuntimeException {

	private final ErrorCode errorCode;

	public CustomRuntimeException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}