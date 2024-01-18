package com.psj.itembrowser.member.service;

import com.psj.itembrowser.member.domain.dto.request.MemberSignUpRequestDTO;
import com.psj.itembrowser.member.domain.dto.response.MemberResponseDTO;

import java.util.Optional;

/**
 * packageName    : com.psj.itembrowser.member.service
 * fileName       : MemberService
 * author         : ipeac
 * date           : 2024-01-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-06        ipeac       최초 생성
 */
public interface MemberService {
    public Optional<MemberResponseDTO> register(MemberSignUpRequestDTO requestDTO);
}