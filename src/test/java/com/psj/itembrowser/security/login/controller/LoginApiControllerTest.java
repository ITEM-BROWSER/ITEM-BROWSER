package com.psj.itembrowser.security.login.controller;

import com.psj.itembrowser.security.login.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(LoginApiController.class)
@ExtendWith(MockitoExtension.class)
public class LoginApiControllerTest {
    
    private final String BASE_URL = "/v1/api";
    private final String EXIST_USER_EMAIL = "qkrtkdwns3410@naver.com";
    private final String EXIST_USER_PASSWORD = "jiohioqh123!@#";
    
    @MockBean
    private LoginService loginService;
    
    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }
}