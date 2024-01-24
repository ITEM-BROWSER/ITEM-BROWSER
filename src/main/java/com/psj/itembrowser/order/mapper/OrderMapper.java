package com.psj.itembrowser.order.mapper;

import com.psj.itembrowser.order.domain.dto.request.OrderDeleteRequestDTO;
import com.psj.itembrowser.order.domain.dto.request.OrderPageRequestDTO;
import com.psj.itembrowser.order.domain.dto.request.OrderRequestDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {
    
    void deleteSoftly(@Param("orderDeleteRequestDTO") OrderDeleteRequestDTO requestDTO);
    
    Order selectOrder(@Param("orderRequestDTO") OrderRequestDTO orderRequestDTO);
    
    List<Order> selectOrders(@Param("orderPageRequestDTO") OrderPageRequestDTO orderPageRequestDTO);
    
    
    Order selectOrderForUpdate(@Param("orderId") long orderId);
}