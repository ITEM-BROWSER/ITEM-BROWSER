package com.psj.itembrowser.security.common.exception;

import lombok.Getter;

/**
 * packageName    : com.psj.itembrowser.login.service.impl
 * fileName       : TokenException
 * author         : ipeac
 * date           : 2024-01-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-15        ipeac       최초 생성
 */
@Getter
public class TokenException extends CustomRuntimeException {
	public TokenException(ErrorCode e) {
		super(e);
	}
}