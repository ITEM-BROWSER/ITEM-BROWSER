package com.psj.itembrowser.order.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.psj.itembrowser.order.domain.dto.request.OrderDeleteRequestDTO;
import com.psj.itembrowser.order.domain.dto.request.OrderRequestDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@MybatisTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:sql/member/insert_member.sql",
    "classpath:sql/shippinginfo/insert_shipping_info.sql",
    "classpath:sql/product/insert_product.sql", "classpath:sql/order/insert_order_product.sql",
    "classpath:sql" + "/order/insert_order.sql"})
public class OrderDeleteMapperTest {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Test
    @DisplayName("주문에 대한 삭제일 업데이트시 true 를 반환하는지 테스트")
    void When_DeleteSoftly_Expect_MethodReturnTrue() {
        // given as @Sql
        long orderIdThatMustSuccess = 1L;
        OrderDeleteRequestDTO requestDTO = OrderDeleteRequestDTO.builder()
            .id(orderIdThatMustSuccess)
            .orderStatus(OrderStatus.CANCELED)
            .build();
        
        // when - then
        assertThatCode(() -> orderMapper.deleteSoftly(requestDTO)).doesNotThrowAnyException();
    }
    
    @Test
    @DisplayName("주문에 대한 삭제일 업데이트시 삭제일이 업데이트 되는지 테스트")
    void When_DeleteSoftly_Expect_DeletedDateUpdated() {
        // given as @Sql
        long orderIdThatMustSuccess = 1L;
        OrderDeleteRequestDTO orderDeleteRequestDTO = OrderDeleteRequestDTO.builder()
            .id(orderIdThatMustSuccess)
            .orderStatus(OrderStatus.CANCELED)
            .build();
        OrderRequestDTO requestDTO = OrderRequestDTO.create(orderIdThatMustSuccess, true);
        
        // when
        orderMapper.deleteSoftly(orderDeleteRequestDTO);
        Order deletedOrder = orderMapper.selectOrder(requestDTO);
        
        // then
        assertThat(deletedOrder).isNotNull();
        assertThat(deletedOrder.getDeletedDate()).isNotNull();
    }
}