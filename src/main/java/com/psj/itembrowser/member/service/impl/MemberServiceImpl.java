package com.psj.itembrowser.member.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.psj.itembrowser.member.domain.dto.request.MemberSignUpRequestDTO;
import com.psj.itembrowser.member.domain.dto.response.MemberResponseDTO;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.member.mapper.MemberMapper;
import com.psj.itembrowser.member.persistence.MemberPersistance;
import com.psj.itembrowser.member.service.MemberService;
import com.psj.itembrowser.security.common.exception.BadRequestException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * packageName    : com.psj.itembrowser.member.service.impl
 * fileName       : MemberServiceImpl
 * author         : ipeac
 * date           : 2024-01-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-06        ipeac       최초 생성
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
	private final MemberMapper memberMapper;
	private final MemberPersistance memberPersistance;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional(readOnly = false)
	public Optional<MemberResponseDTO> register(MemberSignUpRequestDTO requestDTO) {
		String encodedPassword = passwordEncoder.encode(requestDTO.getCredentialsPassword());
		requestDTO.setCredentialsPassword(encodedPassword);
		requestDTO.setMemberShipType(Member.MemberShipType.REGULAR);
		try {
			Long insertedMemberno = memberPersistance.insertMember(requestDTO);
			return Optional.of(memberPersistance.findById(insertedMemberno));
		} catch (BadRequestException e) {
			log.error("BadRequestException : MemberServiceImpl.register() : {}", e.getMessage());
		}
		return Optional.empty();
	}
}