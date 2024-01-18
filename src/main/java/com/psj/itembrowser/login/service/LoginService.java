package com.psj.itembrowser.login.service;

import com.psj.itembrowser.login.domain.dto.request.LoginRequestDTO;
import com.psj.itembrowser.login.domain.dto.response.LoginResponseDTO;
import com.psj.itembrowser.token.domain.dto.TokenPairDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public interface LoginService {
    LoginResponseDTO login(LoginRequestDTO requestDTO);
    
    TokenPairDTO refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}