package com.psj.itembrowser.order.mapper;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Objects;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelation;
import com.psj.itembrowser.product.domain.vo.Product;

@MybatisTest
@Transactional
@AutoConfigureTestDatabase(replace = Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
@Sql(scripts = {"classpath:drop-table.sql", "classpath:schema.sql",
    "classpath:sql/h2/member/insert_member.sql",
    "classpath:sql/h2/shippinginfo/insert_shipping_info.sql",
    "classpath:sql/h2/product/insert_product.sql",
    "classpath:sql/h2/order/insert_order_product.sql",
    "classpath:sql/h2/order/insert_order.sql"})
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
            .orderStatus(Order.OrderStatus.CANCELED)
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
            .orderStatus(Order.OrderStatus.CANCELED)
            .build();

        // when
        orderMapper.deleteSoftly(orderDeleteRequestDTO);
        Order deletedOrder = orderMapper.selectOrderWithNoCondition(orderIdThatMustSuccess);

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
        ThrowingCallable throwingCallable = () -> orderMapper.deleteSoftlyOrderProducts(
            orderIdThatMustSuccess);

        //then
        assertThatCode(throwingCallable)
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("주문-상품 관계를 삭제시 삭제일이 업데이트 되는지")
    void When_DeleteOrderProduct_Expect_DeletedDateUpdated() {
        // given as @Sql
        long orderId = 1L;
        OrdersProductRelation expectedOrderProductRelation = mock(OrdersProductRelation.class);
        given(expectedOrderProductRelation.getGroupId()).willReturn(orderId);
        given(expectedOrderProductRelation.getProductId()).willReturn(1L);
        given(expectedOrderProductRelation.getProductQuantity()).willReturn(10);
        given(expectedOrderProductRelation.getProduct()).willReturn(mock(Product.class));

        OrdersProductRelation expectedOrderProductRelation2 = mock(OrdersProductRelation.class);
        given(expectedOrderProductRelation2.getGroupId()).willReturn(orderId);
        given(expectedOrderProductRelation2.getProductId()).willReturn(2L);
        given(expectedOrderProductRelation2.getProductQuantity()).willReturn(20);
        given(expectedOrderProductRelation2.getProduct()).willReturn(mock(Product.class));

        // when
        orderMapper.deleteSoftlyOrderProducts(orderId);
        List<OrdersProductRelation> ordersProductRelations = orderMapper.selectOrderProductRelations(
            orderId);

        // then
        assertThat(ordersProductRelations).isNotNull();
        assertThat(ordersProductRelations).isNotEmpty();
        assertThat(ordersProductRelations.size()).isEqualTo(2);

        assertThat(ordersProductRelations.get(0).getProductId()).isEqualTo(1L);
        assertThat(ordersProductRelations.get(0).getProductQuantity()).isEqualTo(10);

        assertThat(ordersProductRelations.get(1).getProductId()).isEqualTo(2L);
        assertThat(ordersProductRelations.get(1).getProductQuantity()).isEqualTo(20);

        assertThat(ordersProductRelations.stream()
            .map(OrdersProductRelation::getDeletedDate)
            .allMatch(Objects::nonNull)).isTrue();
    }
}