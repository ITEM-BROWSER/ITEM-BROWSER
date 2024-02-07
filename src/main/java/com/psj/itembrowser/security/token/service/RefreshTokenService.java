package com.psj.itembrowser.security.token.service;

import org.springframework.stereotype.Service;

import com.psj.itembrowser.security.token.domain.vo.RefreshToken;

/**
 * packageName    : com.psj.itembrowser.token.service
 * fileName       : RefreshTokenService
 * author         : ipeac
 * date           : 2024-01-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-16        ipeac       최초 생성
 */
@Service
public interface RefreshTokenService {

	public RefreshToken createRefreshToken(RefreshToken refreshToken);

	RefreshToken getRefreshToken(String email);
}