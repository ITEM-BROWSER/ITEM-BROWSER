package com.psj.itembrowser.common.filter;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.psj.itembrowser.common.config.jwt.JwtProvider;

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
		if (Objects.nonNull(authorization)) {
			String atk = authorization.substring(7);
			try {
				String email = jwtProvider.extractUserEmail(atk);
				UserDetails userDetails = userDetailsService.loadUserByUsername(email);
				Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "",
					userDetails.getAuthorities());
				SecurityContextHolder.getContext()
					.setAuthentication(authentication);
			} catch (JwtException e) {
				request.setAttribute("exception", e.getMessage());
			}
		}
		filterChain.doFilter(request, response);
	}
}