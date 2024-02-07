package com.psj.itembrowser.security.login.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.psj.itembrowser.security.login.domain.dto.request.LoginRequestDTO;
import com.psj.itembrowser.security.login.domain.dto.response.LoginResponseDTO;
import com.psj.itembrowser.security.login.service.LoginService;
import com.psj.itembrowser.security.token.domain.dto.TokenPairDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginApiController {

	private final LoginService loginService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
		LoginResponseDTO loginResponseDTO = loginService.login(loginRequestDTO);

		return ResponseEntity.ok(loginResponseDTO);
	}

	@PutMapping("/refresh-token")
	public ResponseEntity<TokenPairDTO> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		TokenPairDTO tokenPairDTO = loginService.refreshToken(request, response);

		return ResponseEntity.ok(tokenPairDTO);
	}
}