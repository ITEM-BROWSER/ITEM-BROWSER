package com.psj.itembrowser.security.login.service.impl;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.psj.itembrowser.security.common.config.jwt.JwtProvider;
import com.psj.itembrowser.security.common.exception.ErrorCode;
import com.psj.itembrowser.security.common.exception.TokenException;
import com.psj.itembrowser.security.login.domain.dto.request.LoginRequestDTO;
import com.psj.itembrowser.security.login.domain.dto.response.LoginResponseDTO;
import com.psj.itembrowser.security.login.service.LoginService;
import com.psj.itembrowser.security.service.impl.UserDetailsServiceImpl;
import com.psj.itembrowser.security.service.impl.UserDetailsServiceImpl.CustomUserDetails;
import com.psj.itembrowser.security.token.domain.dto.TokenPairDTO;
import com.psj.itembrowser.security.token.domain.vo.RefreshToken;
import com.psj.itembrowser.security.token.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

	private final JwtProvider jwtProvider;
	private final UserDetailsServiceImpl userDetailsService;
	private final RefreshTokenService refreshTokenService;

	@Override
	@Transactional(readOnly = false)
	public LoginResponseDTO login(LoginRequestDTO requestDTO) {
		try {
			CustomUserDetails details = (CustomUserDetails)userDetailsService.loadUserByUsername(requestDTO.getEmail());

			String accessToken = jwtProvider.generateAccessToken(details)
				.orElseThrow(() ->
					new TokenException(ErrorCode.ACCESS_TOKEN_NOT_GENERATED));
			String refreshTokenNumber = jwtProvider.generateRefreshToken(details)
				.orElseThrow(() ->
					new TokenException(ErrorCode.REFRESH_TOKEN_NOT_GENERATED));

			Optional<RefreshToken> refreshToken = getOrUpdateRefreshToken(requestDTO, refreshTokenNumber);

			if (refreshToken.isPresent()) {
				return new LoginResponseDTO(accessToken, refreshToken.get()
					.getRefreshToken());
			}

		} catch (UsernameNotFoundException e) {
			log.error("UsernameNotFoundException : LoginServiceImpl.login() : {}", e.getMessage());
		} catch (TokenException e) {
			log.error("TokenException : LoginServiceImpl.login() : {}", e.getMessage());
		} catch (RuntimeException e) {
			log.error("Runtime : LoginServiceImpl.login() : {}", e.getMessage());
		}

		throw new RuntimeException("login fail");
	}

	@Override
	public TokenPairDTO refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new TokenException(ErrorCode.ACCESS_TOKEN_NOT_GENERATED);
		}

		refreshToken = authHeader.substring(7);
		userEmail = jwtProvider.extractUserEmail(refreshToken);
		if (userEmail != null) {
			UserDetails details = userDetailsService.loadUserByUsername(userEmail);
			if (jwtProvider.isValidToken(refreshToken, details)) {
				String accessToken = jwtProvider.generateAccessToken(details)
					.orElseThrow(() ->
						new TokenException(ErrorCode.ACCESS_TOKEN_NOT_GENERATED));

				return new TokenPairDTO(accessToken, refreshToken);
			}
		}
		return null;
	}

	private Optional<RefreshToken> getOrUpdateRefreshToken(LoginRequestDTO requestDTO, String refreshTokenNumber) {
		RefreshToken existRefreshToken = refreshTokenService.getRefreshToken(requestDTO.getEmail());
		if (existRefreshToken == null) {
			RefreshToken refreshToken = RefreshToken.create(refreshTokenNumber, requestDTO.getEmail());

			refreshTokenService.createRefreshToken(refreshToken);

			return Optional.of(refreshToken);
		}

		return Optional.of(existRefreshToken);
	}
}