package com.psj.itembrowser.common.exception;

import lombok.NonNull;

/**
 * packageName    : com.psj.itembrowser.common.exception
 * fileName       : NotAuthorizedException
 * author         : ipeac
 * date           : 2024-01-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-30        ipeac       최초 생성
 */
public class NotAuthorizedException extends CustomAuthenticationException {
	
	public NotAuthorizedException(@NonNull ErrorCode e) {
		super(e);
	}
}