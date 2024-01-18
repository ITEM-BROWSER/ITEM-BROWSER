package com.psj.itembrowser.order.controller;

import com.psj.itembrowser.common.message.MessageDTO;
import com.psj.itembrowser.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.text.MessageFormat.format;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/order")
public class OrderApiController {
    private final OrderService orderService;
    
    @DeleteMapping("/{orderId}")
    public MessageDTO removeOrder(@PathVariable Long orderId) {
        orderService.removeOrder(orderId);
        return new MessageDTO(format("Order record for {0} has been deleted.", orderId));
    }
}