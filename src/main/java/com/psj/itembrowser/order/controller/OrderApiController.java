package com.psj.itembrowser.order.controller;

import static java.text.MessageFormat.format;

import com.psj.itembrowser.common.message.MessageDTO;
import com.psj.itembrowser.order.domain.dto.response.OrderResponseDTO;
import com.psj.itembrowser.order.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/api/orders")
public class OrderApiController {
    
    private final OrderService orderService;
    
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long orderId) {
        log.info("getOrder");
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }
    
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrders() {
        log.info("getOrders");
        return ResponseEntity.ok(orderService.getOrders());
    }
    
    @PostMapping
    public MessageDTO createOrder() {
        log.info("createOrder");
        orderService.createOrder();
        return new MessageDTO("Order record for {0} has been deleted.");
    }
    
    @PatchMapping("/{orderId}/cancel")
    public MessageDTO cancelOrder(@PathVariable Long orderId) {
        log.info("cancelOrder");
        orderService.cancelOrder(orderId);
        return new MessageDTO(format("Order record for {0} has been deleted.", orderId));
    }
    
    @PostMapping("/{orderId}/refund")
    public MessageDTO refundOrder(@PathVariable Long orderId) {
        orderService.refundOrder(orderId);
        return new MessageDTO(format("Order record for {0} has been deleted.", orderId));
    }
    
    @PostMapping("/{orderId}/exchange")
    public MessageDTO exchangeOrder(@PathVariable Long orderId) {
        log.info("exchangeOrder");
        orderService.exchangeOrder(orderId);
        return new MessageDTO(format("Order record for {0} has been deleted.", orderId));
    }
    
    @DeleteMapping("/{orderId}")
    public MessageDTO removeOrder(@PathVariable Long orderId) {
        log.info("removeOrder");
        orderService.removeOrder(orderId);
        return new MessageDTO(format("Order record for {0} has been deleted.", orderId));
    }
}