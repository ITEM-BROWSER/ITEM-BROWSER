package com.psj.itembrowser.order.domain.vo;

import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.domain.dto.response.OrderResponseDTO;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "ordererId", "orderStatus"})
@ToString
public class Order implements Cancelable {
    
    private Long id;
    private Long ordererId;
    private OrderStatus orderStatus;
    private LocalDateTime paidDate;
    private Long shippingInfoId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime deletedDate;
    private List<OrdersProductRelation> products;
    private Member member;
    private ShippingInfo shippingInfo;
    
    @Builder
    public static Order createOrder(
        Long id, Long ordererId, OrderStatus orderStatus, LocalDateTime paidDate,
        Long shippingInfoId, LocalDateTime createdDate, LocalDateTime updatedDate,
        LocalDateTime deletedDate, List<OrdersProductRelation> products, Member member,
        ShippingInfo shippingInfo
    ) {
        Order order = new Order();
        order.id = id;
        order.ordererId = ordererId;
        order.orderStatus = orderStatus;
        order.paidDate = paidDate;
        order.shippingInfoId = shippingInfoId;
        order.createdDate = createdDate;
        order.updatedDate = updatedDate;
        order.deletedDate = deletedDate;
        order.products = products;
        order.member = member;
        order.shippingInfo = shippingInfo;
        return order;
    }
    
    @Override
    public boolean isNotCancelable() {
        List<OrderStatus> cancelableStatus = List.of(OrderStatus.ACCEPT, OrderStatus.INSTRUCT);
        return cancelableStatus.stream()
            .noneMatch(orderStatus::equals);
    }
    
    public OrderResponseDTO toOrderResponseDTO() {
        return OrderResponseDTO.create(this);
    }
}