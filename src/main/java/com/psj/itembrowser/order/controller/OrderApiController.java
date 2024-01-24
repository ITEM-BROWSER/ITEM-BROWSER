package com.psj.itembrowser.order.controller;

import static java.text.MessageFormat.format;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.psj.itembrowser.common.message.MessageDTO;
import com.psj.itembrowser.order.domain.dto.request.OrderPageRequestDTO;
import com.psj.itembrowser.order.domain.dto.request.OrderRequestDTO;
import com.psj.itembrowser.order.domain.dto.response.OrderResponseDTO;
import com.psj.itembrowser.order.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
        
        OrderRequestDTO orderRequestDTO = OrderRequestDTO.create(orderId, false);
        OrderResponseDTO findOrder = orderService.getOrder(orderRequestDTO);
        
        log.info("getOrder is completed");
        return ResponseEntity.ok(findOrder);
    }
    
    @GetMapping("")
    public ResponseEntity<PageInfo<OrderResponseDTO>> getOrders(Pageable pageable,
        OrderPageRequestDTO orderRequestDTO) {
        log.info("getOrders");
        
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<OrderResponseDTO> orders = orderService.getOrders(orderRequestDTO);
        
        log.info("getOrders is completed");
        return ResponseEntity.ok(new PageInfo<>(orders));
    }
    
    @PostMapping("")
    public MessageDTO createOrder() {
        log.info("createOrder");
        orderService.createOrder();
        log.info("createOrder is completed");
        return new MessageDTO("Order record for {0} has been deleted.");
    }
    
    @PatchMapping("/{orderId}/cancel")
    public MessageDTO cancelOrder(@PathVariable Long orderId) {
        log.info("cancelOrder");
        
        orderService.cancelOrder(orderId);
        
        log.info("cancelOrder is completed");
        
        return new MessageDTO(format("Order record for {0} has been deleted.", orderId));
    }
    
    @PostMapping("/{orderId}/refund")
    public MessageDTO refundOrder(@PathVariable Long orderId) {
        log.info("refundOrder");
        
        orderService.refundOrder(orderId);
        
        log.info("refundOrder is completed");
        
        return new MessageDTO(format("Order record for {0} has been deleted.", orderId));
    }
    
    @PostMapping("/{orderId}/exchange")
    public MessageDTO exchangeOrder(@PathVariable Long orderId) {
        log.info("exchangeOrder");
        
        orderService.exchangeOrder(orderId);
        
        log.info("exchangeOrder is completed");
        
        return new MessageDTO(format("Order record for {0} has been deleted.", orderId));
    }
    
    @DeleteMapping("/{orderId}")
    public MessageDTO removeOrder(@PathVariable Long orderId) {
        log.info("removeOrder");
        
        orderService.removeOrder(orderId);
        
        log.info("removeOrder is completed");
        
        return new MessageDTO(format("Order record for {0} has been deleted.", orderId));
    }
}