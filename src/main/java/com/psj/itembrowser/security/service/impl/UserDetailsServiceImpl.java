package com.psj.itembrowser.security.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.psj.itembrowser.member.domain.dto.response.MemberResponseDTO;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.member.persistence.MemberPersistance;
import com.psj.itembrowser.security.common.exception.ErrorCode;
import com.psj.itembrowser.security.common.exception.TokenException;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberPersistance memberPersistance;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("loadUserByUsername : {}", email);

		MemberResponseDTO memberResponseDTO = this.memberPersistance.findByEmail(email);

		if (memberResponseDTO == null) {
			throw new UsernameNotFoundException("username " + email + " is not found");
		}

		return new CustomUserDetails(memberResponseDTO);
	}

	public CustomUserDetails loadUserByJwt(@NonNull Jwt jwt) {
		if (Objects.isNull(jwt.getSubject())) {
			throw new TokenException(ErrorCode.NOT_FOUND_SUBJECT);
		}

		return (CustomUserDetails)this.loadUserByUsername(jwt.getSubject());
	}

	@Getter
	@RequiredArgsConstructor
	public static final class CustomUserDetails implements UserDetails {

		private final MemberResponseDTO memberResponseDTO;

		/*
		 * GrantedAuthority : 인증된 사용자의 권한을 나타낸다.
		 * 일반적으로 ROLE_USER , ROLE_ADMIN 같이 "ROLE_"로 시작하는 문자열이다.
		 * */
		private static final List<GrantedAuthority> AUTHORITIES =
			Collections.unmodifiableList(
				AuthorityUtils.createAuthorityList(
					Arrays.stream(Member.Role.values())
						.map(Enum::name)
						.toArray(String[]::new)
				)
			);

		@Override
		public Collection<GrantedAuthority> getAuthorities() {
			return AUTHORITIES;
		}

		@Override
		public String getPassword() {
			return memberResponseDTO.getPassword();
		}

		@Override
		public String getUsername() {
			return memberResponseDTO.getEmail();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}
}