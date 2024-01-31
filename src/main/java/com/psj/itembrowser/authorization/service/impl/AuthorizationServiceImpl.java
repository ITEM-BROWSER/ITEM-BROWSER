package com.psj.itembrowser.authorization.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.psj.itembrowser.authorization.service.AuthorizationService;
import com.psj.itembrowser.common.exception.ErrorCode;
import com.psj.itembrowser.common.exception.NotAuthorizedException;
import com.psj.itembrowser.member.domain.dto.response.MemberResponseDTO;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.security.service.impl.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {
	
	private final UserDetailsServiceImpl userDetailsService;
	
	@Override
	public void authorizeOrder(Order order) {
		MemberResponseDTO principal = getPrincipal();
		
		if (principal == null) {
			log.error("principal is null");
			
			throw new NotAuthorizedException(ErrorCode.PRINCIPAL_NOT_FOUND);
		}
		
		Member member = Member.from(principal);
		
		if (member.hasRole(Member.Role.ROLE_STORE_SELLER)) {
			throw new NotAuthorizedException(ErrorCode.SELLER_NOT_AUTHORIZED);
		}
		
		if (member.hasRole(Member.Role.ROLE_ADMIN)) {
			return;
		}
		
		if (member.hasRole(Member.Role.ROLE_CUSTOMER)) {
			if (member.isSame(order.getMember())) {
				return;
			}
			
			throw new NotAuthorizedException(ErrorCode.CUSTOMER_NOT_AUTHORIZED);
		}
	}
	
	private MemberResponseDTO getPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			log.error("authentication is null");
			
			throw new NotAuthorizedException(ErrorCode.PRINCIPAL_NOT_FOUND);
		}
		
		UserDetailsServiceImpl.CustomUserDetails details = (UserDetailsServiceImpl.CustomUserDetails)userDetailsService.loadUserByUsername(
			authentication.getName());
		
		return details.getMemberResponseDTO();
	}
}