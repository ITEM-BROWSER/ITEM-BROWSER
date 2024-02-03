package com.psj.itembrowser.security.token.service.impl;

import com.psj.itembrowser.security.token.domain.vo.RefreshToken;
import com.psj.itembrowser.security.token.mapper.RefreshTokenMapper;
import com.psj.itembrowser.security.token.service.RefreshTokenService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : com.psj.itembrowser.token.service.impl
 * fileName       : RefreshTokenServiceImpl
 * author         : ipeac
 * date           : 2024-01-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-16        ipeac       최초 생성
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    
    private final RefreshTokenMapper refreshTokenMapper;
    
    @Override
    public RefreshToken createRefreshToken(RefreshToken refreshToken) {
        refreshTokenMapper.createRefreshToken(refreshToken);
        return null;
    }
    
    public RefreshToken getRefreshToken(String email) {
        Optional<RefreshToken> refreshToken = refreshTokenMapper.getRefreshTokenByEmail(email);
        return refreshToken.orElse(null);
    }
}