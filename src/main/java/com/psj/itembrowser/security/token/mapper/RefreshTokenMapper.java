package com.psj.itembrowser.security.token.mapper;


import com.psj.itembrowser.security.token.domain.vo.RefreshToken;
import java.util.Optional;
import lombok.NonNull;
import org.apache.ibatis.annotations.Mapper;

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