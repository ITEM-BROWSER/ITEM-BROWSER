package com.psj.itembrowser.order.controller;

import static java.text.MessageFormat.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.psj.itembrowser.common.message.MessageDTO;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.domain.dto.OrderResponseDTO;
import com.psj.itembrowser.order.service.OrderService;
import com.psj.itembrowser.security.service.impl.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderApiController {
	
	private final OrderService orderService;
	
	@PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_ADMIN')")
	@PostAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_CUSTOMER') and returnObject.body.member.email == principal.username)")
	@GetMapping("/v1/api/orders/{orderId}")
	public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long orderId,
		@AuthenticationPrincipal UserDetailsServiceImpl.CustomUserDetails userDetails) {
		log.info("getOrder : {}", orderId);
		log.info("userDetails : {}", userDetails);
		
		Member member = Member.from(userDetails.getMemberResponseDTO());
		
		OrderResponseDTO dto = null;
		
		if (member.hasRole(Member.Role.ROLE_CUSTOMER)) {
			dto = orderService.getOrderWithNotDeleted(orderId);
		} else if (member.hasRole(Member.Role.ROLE_ADMIN)) {
			dto = orderService.getOrderWithNoCondition(orderId);
		}
		
		return ResponseEntity.ok(dto);
	}
	
	@DeleteMapping("/v1/api/orders/{orderId}")
	public MessageDTO removeOrder(@PathVariable Long orderId) {
		orderService.removeOrder(orderId);
		return new MessageDTO(format("Order record for {0} has been deleted.", orderId));
	}
}