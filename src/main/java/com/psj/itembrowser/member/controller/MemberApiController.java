package com.psj.itembrowser.member.controller;

import com.psj.itembrowser.member.domain.dto.request.MemberSignUpRequestDTO;
import com.psj.itembrowser.member.domain.dto.response.MemberResponseDTO;
import com.psj.itembrowser.member.service.MemberService;
import com.psj.itembrowser.security.common.message.MessageDTO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.psj.itembrowser.member.web
 * fileName       : MemberApiController
 * author         : ipeac
 * date           : 2024-01-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-13        ipeac       최초 생성
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/member")
public class MemberApiController {
    private final MemberService memberService;
    
    @PostMapping("/register")
    public MessageDTO register(MemberSignUpRequestDTO dto) {
        Optional<MemberResponseDTO> register = memberService.register(dto);
        if (register.isEmpty()) {
            log.error("member register fail");
            return new MessageDTO("member register fail");
        }
        log.info("member register success");
        return new MessageDTO("member register success");
    }
    
}