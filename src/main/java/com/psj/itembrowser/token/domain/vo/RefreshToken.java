package com.psj.itembrowser.token.domain.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * packageName    : com.psj.itembrowser.security.domain
 * fileName       : RefreshToken
 * author         : ipeac
 * date           : 2024-01-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-14        ipeac       최초 생성
 */
@Getter
@NoArgsConstructor
public class RefreshToken {
    private Long id;
    @NotBlank
    private String refreshToken;
    @NotBlank
    private String email;
    
    private LocalDateTime createdDate;
    
    public RefreshToken(String refreshToken, String email) {
        this.refreshToken = refreshToken;
        this.email = email;
        this.createdDate = LocalDateTime.now();
    }
    
    public static RefreshToken create(String refreshToken, String email) {
        return new RefreshToken(refreshToken, email);
    }
    
    public RefreshToken update(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}