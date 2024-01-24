package com.psj.itembrowser.order.mapper;

import com.psj.itembrowser.order.domain.vo.OrdersProductRelation;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderProductRelationMapper {
    
    void deleteSoftlyOrderProducts(@Param("orderId") long orderId);
    
    List<OrdersProductRelation> selectOrderProductRelations(@Param("orderId") long orderId);
}