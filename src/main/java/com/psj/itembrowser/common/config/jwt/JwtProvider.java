package com.psj.itembrowser.common.config.jwt;

import com.psj.itembrowser.token.mapper.RefreshTokenMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final RefreshTokenMapper refreshTokenMapper;
    private final JwtProperties jwtProperties;
    
    public Optional<String> generateAccessToken(UserDetails authentication) {
        return generateToken(authentication, jwtProperties.getAccessTokenExpirationTime());
    }
    
    public Optional<String> generateRefreshToken(UserDetails authentication) {
        return generateToken(authentication, jwtProperties.getRefreshTokenExpirationTime());
    }
    
    private Optional<String> generateToken(final UserDetails userDetails, Long tokenExpirationtime) {
        log.info("authentication : {}", userDetails);
        log.info("refreshTokenExpirationTime : {}", tokenExpirationtime);
        Instant now = Instant.now();
        
        String scope = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(tokenExpirationtime))
                .subject(userDetails.getUsername())
                .claim("scope", scope)
                .build();
        
        log.info(userDetails.getUsername() + " : " + claims.getSubject());
        
        return Optional.ofNullable(this.jwtEncoder.encode(JwtEncoderParameters.from(claims))
                .getTokenValue());
    }
    
    public String extractUserEmail(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getSubject();
    }
    
    public boolean isValidToken(String token, UserDetails details) {
        String email = extractUserEmail(token);
        return Objects.equals(email, details.getUsername()) && isTokenExpired(token) == false;
    }
    
    private boolean isTokenExpired(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        Instant expiresAt = jwt.getExpiresAt();
        return Objects.requireNonNull(expiresAt)
                .isBefore(Instant.now());
    }
    
    public String getClaims(String accessToken) {
        Jwt jwt = jwtDecoder.decode(accessToken);
        return jwt.getSubject();
    }
}