package com.psj.itembrowser.security.login.service;

import com.psj.itembrowser.security.login.domain.dto.request.LoginRequestDTO;
import com.psj.itembrowser.security.login.domain.dto.response.LoginResponseDTO;
import com.psj.itembrowser.security.token.domain.dto.TokenPairDTO;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    LoginResponseDTO login(LoginRequestDTO requestDTO);
    
    TokenPairDTO refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}