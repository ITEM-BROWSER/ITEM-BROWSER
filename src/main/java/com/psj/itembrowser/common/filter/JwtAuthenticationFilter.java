package com.psj.itembrowser.common.filter;

import com.psj.itembrowser.common.config.jwt.JwtProvider;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * packageName    : com.psj.itembrowser.common.filter fileName       : CustomAuthenticationFilter
 * author         : ipeac date           : 2024-01-14 description    :
 * =========================================================== DATE              AUTHOR NOTE
 * ----------------------------------------------------------- 2024-01-14        ipeac       최초 생성
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
                String email = jwtProvider.extractUserEmail(atk).trim();
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                Authentication token = new UsernamePasswordAuthenticationToken(userDetails, "",
                    userDetails.getAuthorities());
                SecurityContextHolder.getContext()
                    .setAuthentication(token);
            } catch (JwtException e) {
                request.setAttribute("exception", e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}