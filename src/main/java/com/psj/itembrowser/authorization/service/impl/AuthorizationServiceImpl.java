package com.psj.itembrowser.authorization.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.psj.itembrowser.authorization.service.AuthorizationService;
import com.psj.itembrowser.common.exception.ErrorCode;
import com.psj.itembrowser.common.exception.NotAuthorizedException;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.security.service.impl.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {
	
	private final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	
	@Override
	public void authorizeOrder(Order order) {
		UserDetailsServiceImpl.CustomUserDetails principal = getPrincipal();
		Member member = Member.from(principal.getMemberResponseDTO());
		
		if (principal.hasRole(Member.Role.ROLE_STORE_SELLER)) {
			throw new NotAuthorizedException(ErrorCode.SELLER_NOT_AUTHORIZED);
		}
		
		if (principal.hasRole(Member.Role.ROLE_ADMIN)) {
			return;
		}
		
		if (principal.hasRole(Member.Role.ROLE_CUSTOMER)) {
			if (member.isSame(order.getMember())) {
				return;
			}
			
			throw new NotAuthorizedException(ErrorCode.CUSTOMER_NOT_AUTHORIZED);
		}
	}
	
	private UserDetailsServiceImpl.CustomUserDetails getPrincipal() {
		UserDetailsServiceImpl.CustomUserDetails principal = (UserDetailsServiceImpl.CustomUserDetails)authentication.getPrincipal();
		
		if (principal == null) {
			throw new NotAuthorizedException(ErrorCode.PRINCIPAL_NOT_FOUND);
		}
		
		return principal;
	}
}