package com.psj.itembrowser.security.common.exception;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class NotFoundException extends CustomRuntimeException {
	
	public NotFoundException(@NonNull ErrorCode e) {
		super(e);
	}
	
}