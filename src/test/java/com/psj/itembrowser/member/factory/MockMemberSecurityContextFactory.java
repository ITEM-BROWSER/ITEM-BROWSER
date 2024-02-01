package com.psj.itembrowser.member.factory;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.psj.itembrowser.member.annotation.MockMember;
import com.psj.itembrowser.member.domain.dto.response.MemberResponseDTO;
import com.psj.itembrowser.member.domain.vo.Address;
import com.psj.itembrowser.member.domain.vo.Credentials;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.member.domain.vo.MemberNo;
import com.psj.itembrowser.member.domain.vo.Name;
import com.psj.itembrowser.security.service.impl.UserDetailsServiceImpl;

/**
 * packageName    : com.psj.itembrowser.member.annotation
 * fileName       : MockMemberSecurityContextFactory
 * author         : ipeac
 * date           : 2024-02-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-01        ipeac       최초 생성
 */
public class MockMemberSecurityContextFactory implements WithSecurityContextFactory<MockMember> {
	
	@Override
	public SecurityContext createSecurityContext(MockMember annotation) {
		Member mockMember = new Member(MemberNo.create(annotation.memberNo()),
			Credentials.create(annotation.email(), annotation.password()),
			Name.create(annotation.firstName(), annotation.lastName()),
			annotation.phoneNumber(),
			annotation.gender(),
			annotation.role(),
			annotation.status(),
			Address.create(annotation.addressMain(), annotation.addressSub(), annotation.zipCode()),
			LocalDate.now(),
			LocalDateTime.now()
		);
		
		UserDetailsServiceImpl.CustomUserDetails customUserDetails = new UserDetailsServiceImpl.CustomUserDetails(
			MemberResponseDTO.from(mockMember));
		
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			customUserDetails, null, customUserDetails.getAuthorities());
		
		SecurityContext context = org.springframework.security.core.context.SecurityContextHolder.createEmptyContext();
		
		context.setAuthentication(authentication);
		
		return context;
	}
}