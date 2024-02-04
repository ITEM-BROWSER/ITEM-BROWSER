package com.psj.itembrowser.order.controller;

import static java.text.MessageFormat.format;

import com.github.pagehelper.PageInfo;
import com.psj.itembrowser.member.annotation.CurrentUser;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.domain.dto.request.OrderPageRequestDTO;
import com.psj.itembrowser.order.domain.dto.response.OrderResponseDTO;
import com.psj.itembrowser.order.service.OrderService;
import com.psj.itembrowser.security.common.message.MessageDTO;
import com.psj.itembrowser.security.service.impl.UserDetailsServiceImpl;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderApiController {
    
    private final OrderService orderService;
    private final UserDetailsServiceImpl userDetailsService;
    
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_ADMIN')")
    @PostAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_CUSTOMER') and returnObject.body.member.email == principal.username)")
    @GetMapping("/v1/api/orders/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long orderId,
        @CurrentUser Jwt jwt) {
        log.info("getOrders orderId : {}", orderId);
        
        UserDetailsServiceImpl.CustomUserDetails customUserDetails = userDetailsService.loadUserByJwt(
            jwt);
        
        Member member = Member.from(customUserDetails.getMemberResponseDTO());
        
        OrderResponseDTO dto = null;
        
        if (member.hasRole(Member.Role.ROLE_CUSTOMER)) {
            dto = orderService.getOrderWithNotDeleted(orderId);
        } else if (member.hasRole(Member.Role.ROLE_ADMIN)) {
            dto = orderService.getOrderWithNoCondition(orderId);
        }
        
        return ResponseEntity.ok(dto);
    }
    
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_ADMIN')")
    @PostAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_CUSTOMER') and returnObject.body.member.email == principal.username)")
    @GetMapping("/v1/api/orders")
    public ResponseEntity<PageInfo<OrderResponseDTO>> getorders(
        @Valid @RequestBody OrderPageRequestDTO requestDTO, @CurrentUser Jwt jwt) {
        log.info("getOrders requestDTO : {}", requestDTO);
        
        UserDetailsServiceImpl.CustomUserDetails customUserDetails = userDetailsService.loadUserByJwt(
            jwt);
        
        Member member = Member.from(customUserDetails.getMemberResponseDTO());
        
        PageInfo<OrderResponseDTO> orderResponseDTOPageInfo = null;
        
        if (member.hasRole(Member.Role.ROLE_ADMIN)) {
            orderResponseDTOPageInfo = orderService.getOrdersWithPaginationAndNoCondition(
                requestDTO);
        } else if (member.hasRole(Member.Role.ROLE_CUSTOMER)) {
            orderResponseDTOPageInfo = orderService.getOrdersWithPaginationAndNotDeleted(requestDTO);
        }
        
        return ResponseEntity.ok(orderResponseDTOPageInfo);
    }
    
    @DeleteMapping("/v1/api/orders/{orderId}")
    public MessageDTO removeOrder(@PathVariable Long orderId) {
        orderService.removeOrder(orderId);
        return new MessageDTO(format("Order record for {0} has been deleted.", orderId));
    }
}