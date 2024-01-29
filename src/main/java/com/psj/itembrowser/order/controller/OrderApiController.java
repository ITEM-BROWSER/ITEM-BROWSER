package com.psj.itembrowser.order.controller;

import static java.text.MessageFormat.format;

import com.psj.itembrowser.common.message.MessageDTO;
import com.psj.itembrowser.order.domain.dto.OrderResponseDTO;
import com.psj.itembrowser.order.domain.dto.request.OrderRequestDTO;
import com.psj.itembrowser.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/orders")
public class OrderApiController {
    
    private final OrderService orderService;
    
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long orderId) {
        OrderRequestDTO orderRequestDTO = OrderRequestDTO.forActiveOrder(orderId);
        return ResponseEntity.ok(orderService.getOrder(orderRequestDTO));
    }
    
    @DeleteMapping("/{orderId}")
    public MessageDTO removeOrder(@PathVariable Long orderId) {
        orderService.removeOrder(orderId);
        return new MessageDTO(format("Order record for {0} has been deleted.", orderId));
    }
}