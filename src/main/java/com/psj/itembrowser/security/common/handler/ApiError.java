package com.psj.itembrowser.security.common.handler;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiError {
	private final int status;
	private final String error;
	private final String message;
	private final String path;
	private final LocalDateTime timestamp = LocalDateTime.now();
}