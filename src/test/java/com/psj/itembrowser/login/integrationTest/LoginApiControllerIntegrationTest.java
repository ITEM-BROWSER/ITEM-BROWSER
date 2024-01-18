package com.psj.itembrowser.login.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psj.itembrowser.login.domain.dto.request.LoginRequestDTO;
import com.psj.itembrowser.member.domain.dto.request.MemberSignUpRequestDTO;
import com.psj.itembrowser.member.domain.dto.response.MemberResponseDTO;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class LoginApiControllerIntegrationTest {
    
    private final static String EXIST_USER_EMAIL = "qkrtkdwns3410@naver.com";
    private final static String EXIST_USER_PASSWORD = "jiohioqh123!@#";
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private MemberService memberService;
    
    private LoginRequestDTO validLoginRequestDTO;
    
    @BeforeEach
    public void setUp() {
        MemberSignUpRequestDTO memberSignUpRequestDTO = new MemberSignUpRequestDTO();
        memberSignUpRequestDTO.setCredentialsEmail(EXIST_USER_EMAIL);
        memberSignUpRequestDTO.setCredentialsPassword(EXIST_USER_PASSWORD);
        memberSignUpRequestDTO.setBirthday(LocalDate.now());
        memberSignUpRequestDTO.setGender(Member.Gender.MEN);
        memberSignUpRequestDTO.setRole(Member.Role.ROLE_CUSTOMER);
        memberSignUpRequestDTO.setContactPhoneNumber("010-1234-5678");
        memberSignUpRequestDTO.setNameFirstName("홍");
        memberSignUpRequestDTO.setNameLastName("길동");
        memberSignUpRequestDTO.setStatus(Member.Status.ACTIVE);

        Optional<MemberResponseDTO> register = memberService.register(memberSignUpRequestDTO);
        
        validLoginRequestDTO = new LoginRequestDTO("qkrtkdwns3410@naver.com", EXIST_USER_PASSWORD);
    }
    
    @Test
    @DisplayName("로그인시 엑세스토큰과 리프레시 토큰 정상 발급")
    public void whenValidLogin_thenReturnsAccessTokenAndRefreshToken() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }
}