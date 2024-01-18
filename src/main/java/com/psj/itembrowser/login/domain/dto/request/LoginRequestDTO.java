package com.psj.itembrowser.login.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    @NotNull(message = "email must be not null")
    private String email;
    
    @NotNull(message = "password must be not null")
    private String password;
}