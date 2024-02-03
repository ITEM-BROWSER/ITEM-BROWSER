package com.psj.itembrowser.security.common.config.jwt;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.psj.itembrowser.common.config.jwt
 * fileName       : JwtProperties
 * author         : ipeac
 * date           : 2024-01-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-16        ipeac       최초 생성
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private PublicKey publicKey = new PublicKey();
    private PrivateKey privateKey = new PrivateKey();
    private AccessToken accessToken = new AccessToken();
    private RefreshToken refreshToken = new RefreshToken();
    
    public Long getRefreshTokenExpirationTime() {
        return refreshToken.getExpirationTime();
    }
    
    public Long getAccessTokenExpirationTime() {
        return accessToken.getExpirationTime();
    }
    
    @Getter
    @Setter
    public static class PublicKey {
        private String key;
    }
    
    @Getter
    @Setter
    public static class PrivateKey {
        private String key;
    }
    
    @Getter
    @Setter
    public static class AccessToken {
        private Long expirationTime;
    }
    
    @Getter
    @Setter
    public static class RefreshToken {
        private Long expirationTime;
    }
}