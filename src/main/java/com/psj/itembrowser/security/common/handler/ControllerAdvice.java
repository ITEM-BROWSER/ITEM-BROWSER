package com.psj.itembrowser.security.common.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.psj.itembrowser.security.common.exception.CustomAuthenticationException;
import com.psj.itembrowser.security.common.exception.CustomIllegalStateException;
import com.psj.itembrowser.security.common.exception.CustomRuntimeException;

@RestControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler(CustomRuntimeException.class)
	public final ResponseEntity<Object> notFoundExceptionHandler(CustomRuntimeException e,
		WebRequest request) {
		ApiError apiError = new ApiError(
			e.getErrorCode().getStatus()
				.value(),
			e.getErrorCode().getMessage(),
			e.getMessage(),
			request.getDescription(false)
		);

		return new ResponseEntity<>(apiError, e.getErrorCode().getStatus());
	}

	@ExceptionHandler(CustomIllegalStateException.class)
	public final ResponseEntity<Object> badRequestExceptionHandler(CustomIllegalStateException e,
		WebRequest request) {
		ApiError apiError = new ApiError(
			e.getErrorCode().getStatus()
				.value(),
			e.getErrorCode().getMessage(),
			e.getMessage(),
			request.getDescription(false)
		);

		return new ResponseEntity<>(apiError, e.getErrorCode().getStatus());
	}

	@ExceptionHandler(CustomAuthenticationException.class)
	public final ResponseEntity<Object> authenticationExceptionHandler(CustomAuthenticationException e,
		WebRequest request) {
		ApiError apiError = new ApiError(
			e.getErrorCode().getStatus()
				.value(),
			e.getErrorCode().getMessage(),
			e.getMessage(),
			request.getDescription(false)
		);

		return new ResponseEntity<>(apiError, e.getErrorCode().getStatus());
	}
}