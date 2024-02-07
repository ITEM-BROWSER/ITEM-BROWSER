package com.psj.itembrowser.member.factory;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.psj.itembrowser.member.annotation.MockMember;

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
		Jwt jwt = Jwt.withTokenValue("token")
			.header("alg", "hs-256")
			.claim("sub", annotation.email())
			.build();

		JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, null, null);

		SecurityContext context = org.springframework.security.core.context.SecurityContextHolder.createEmptyContext();

		context.setAuthentication(authentication);

		return context;
	}
}