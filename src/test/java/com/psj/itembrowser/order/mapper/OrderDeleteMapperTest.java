package com.psj.itembrowser.order.mapper;

import com.psj.itembrowser.common.generator.order.OrderMockDataGenerator;
import com.psj.itembrowser.order.domain.dto.request.OrderRequestDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrderStatus;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelation;
import com.psj.itembrowser.product.domain.vo.Product;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;

@MybatisTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:sql/member/insert_member.sql", "classpath:sql/shippinginfo/insert_shipping_info.sql",
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
        OrderRequestDTO requestDTO = OrderRequestDTO.builder()
                .id(orderIdThatMustSuccess)
                .shownDeletedOrder(true)
                .build();
        
        // when
        orderMapper.deleteSoftly(orderDeleteRequestDTO);
        Order deletedOrder = orderMapper.selectOrder(requestDTO);
        
        // then
        assertThat(deletedOrder).isNotNull();
        assertThat(deletedOrder.getDeletedDate()).isNotNull();
    }
    
    @Test
    @DisplayName("주문-상품 관계를 삭제시 어떠한 에러도 발생하지 않아야함을 테스트")
    void When_DeleteCartProduct_Expect_MethodReturnTrue() {
        // given as @Sql
        long orderIdThatMustSuccess = 1L;
        
        // when
        ThrowingCallable throwingCallable = () -> orderMapper.deleteSoftlyOrderProducts(orderIdThatMustSuccess);
        
        //then
        assertThatCode(throwingCallable)
                .doesNotThrowAnyException();
    }
    
    @Test
    @DisplayName("주문-상품 관계를 삭제시 삭제일이 업데이트 되는지")
    void When_DeleteOrderProduct_Expect_DeletedDateUpdated() {
        // given as @Sql
        long orderId = 1L;
        OrdersProductRelation expectedOrderProductRelation = OrderMockDataGenerator.createOrdersProductRelation(
                orderId,
                1L,
                10,
                mock(Product.class)
        );
        
        OrdersProductRelation expectedOrderProductRelation2 = OrderMockDataGenerator.createOrdersProductRelation(
                orderId,
                2L,
                20,
                mock(Product.class)
        );
        
        // when
        orderMapper.deleteSoftlyOrderProducts(orderId);
        List<OrdersProductRelation> ordersProductRelations = orderMapper.selectOrderProductRelations(orderId);
        
        // then
        assertThat(ordersProductRelations).isNotNull();
        assertThat(ordersProductRelations).isNotEmpty();
        assertThat(ordersProductRelations.size()).isEqualTo(2);
        assertThat(List.of(expectedOrderProductRelation, expectedOrderProductRelation2)).isEqualTo(
                ordersProductRelations);
        assertThat(ordersProductRelations.stream()
                .map(OrdersProductRelation::getDeletedDate)
                .allMatch(Objects::nonNull)).isTrue();
    }
    
}