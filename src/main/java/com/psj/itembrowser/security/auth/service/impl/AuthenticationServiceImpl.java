package com.psj.itembrowser.security.auth.service.impl;

import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.member.domain.vo.Member.Role;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.security.auth.service.AuthenticationService;
import com.psj.itembrowser.security.common.exception.BadRequestException;
import com.psj.itembrowser.security.common.exception.ErrorCode;
import com.psj.itembrowser.security.service.impl.UserDetailsServiceImpl;
import com.psj.itembrowser.security.service.impl.UserDetailsServiceImpl.CustomUserDetails;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.psj.itembrowser.order.service.impl fileName       : AuthenticationService
 * author         : ipeac date           : 2024-02-05 description    :
 * =========================================================== DATE              AUTHOR NOTE
 * ----------------------------------------------------------- 2024-02-05        ipeac       최초 생성
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    
    private final UserDetailsServiceImpl userDetailsService;
    
    @Override
    public void authorizeOrdersWhenCustomer(@NonNull final List<Order> orders) {
        log.info("AuthenticationServiceImpl#authorizeOrders started");
        
        if (orders.isEmpty()) {
            log.info("authorizeOrders orders is empty");
            
            return;
        }
        
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication()
            .getName();
        
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
            currentUserEmail);
        
        Member currentMember = Member.from(customUserDetails.getMemberResponseDTO());
        
        if (currentMember.hasRole(Role.ROLE_CUSTOMER)) {
            log.info("authorizeOrders currentRole : {}", currentMember.getRole());
            
            orders.forEach(order -> {
                if (!currentMember.isSame(order.getMember())) {
                    throw new BadRequestException(ErrorCode.ORDER_IS_NOT_MATCH_CURRENT_MEMBER);
                }
                
                log.info(
                    "authorizeOrders currentMember is same with order member ==> passed");
            });
            
            return;
        }
        
        throw new BadRequestException(ErrorCode.INVALID_MEMBER_ROLE);
    }
}