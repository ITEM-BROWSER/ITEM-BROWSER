package com.psj.itembrowser.security.token.domain.dto;

import com.psj.itembrowser.security.token.domain.vo.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * DTO for {@link RefreshToken}
 */
@Value
@AllArgsConstructor
public class TokenPairDTO {
    String accessToken;
    String refreshToken;
}