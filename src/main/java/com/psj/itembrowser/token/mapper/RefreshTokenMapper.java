package com.psj.itembrowser.token.mapper;


import com.psj.itembrowser.token.domain.vo.RefreshToken;
import lombok.NonNull;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface RefreshTokenMapper {
    
    /**
     * refreshToken을 저장한다.
     *
     * @param refreshToken refreshToken
     */
    Long createRefreshToken(RefreshToken refreshToken);
    
    /**
     * refreshToken을 조회한다.
     *
     * @param email email
     * @return refreshToken
     */
    Optional<RefreshToken> getRefreshTokenByEmail(@NonNull String email);
    
    Optional<RefreshToken> getRefreshTokenByRefreshToken(@NonNull String refreshToken);
}