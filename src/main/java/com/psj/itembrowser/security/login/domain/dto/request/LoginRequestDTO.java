package com.psj.itembrowser.security.login.domain.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    @NotNull(message = "email must be not null")
    private String email;
    
    @NotNull(message = "password must be not null")
    private String password;
}