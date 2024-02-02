package com.psj.itembrowser.common.filter;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import com.psj.itembrowser.common.config.jwt.JwtProvider;
import com.psj.itembrowser.security.service.impl.UserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * packageName    : com.psj.itembrowser.common.filter
 * fileName       : CustomAuthenticationFilter
 * author         : ipeac
 * date           : 2024-01-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-14        ipeac       최초 생성
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		
		String authorization = request.getHeader("Authorization");
		
		if (Objects.nonNull(authorization) && authorization.startsWith("Bearer ")) {
			String atk = authorization.substring(7);
			
			try {
				Jwt jwt = jwtProvider.decodeJwt(atk);
				
				String email = jwt.getSubject();
				
				UserDetailsServiceImpl.CustomUserDetails details = (UserDetailsServiceImpl.CustomUserDetails)userDetailsService.loadUserByUsername(
					email);
				
				JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, details.getAuthorities());
				
				SecurityContext context = org.springframework.security.core.context.SecurityContextHolder.createEmptyContext();
				
				context.setAuthentication(authentication);
				
			} catch (JwtException e) {
				request.setAttribute("exception", e.getMessage());
			}
		}
		
		filterChain.doFilter(request, response);
	}
}