package com.psj.itembrowser.order.mapper;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.domain.dto.request.OrderRequestDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrderStatus;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelation;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;

@MybatisTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:drop-table.sql", "classpath:schema.sql",
	"classpath:sql/member/insert_member.sql", "classpath:sql/shippinginfo/insert_shipping_info.sql",
	"classpath:sql/product/insert_product.sql", "classpath:sql/order/insert_order_product.sql",
	"classpath:sql/order/insert_order.sql"})
public class OrderSelectMapperTest {
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Test
	@DisplayName("1번 주문을 조회시 기대한 1번 주문과 같은 주문이 조회되는지 테스트")
	void When_SelectOrder_Expect_Order1() {
		// given as @Sql
		Order expectedOrder = Order.createOrder(
			1L,
			1L,
			OrderStatus.ACCEPT,
			LocalDateTime.now(),
			1L,
			LocalDateTime.now(),
			null,
			null,
			List.of(
				mock(OrdersProductRelation.class),
				mock(OrdersProductRelation.class)
			),
			mock(Member.class),
			mock(ShippingInfo.class)
		);
		
		OrderRequestDTO requestDTO = OrderRequestDTO.builder()
			.id(1L)
			.shownDeletedOrder(false)
			.build();
		
		// when
		Order order = orderMapper.selectOrder(requestDTO);
		
		// then
		assertThat(order).isNotNull();
		assertThat(order).isEqualTo(expectedOrder);
	}
	
	@Test
	@DisplayName("비관적 락을 걸고 주문을 조회시 주문이 존재하면 주문을 반환하는지 테스트")
	void When_SelectOrderForUpdate_Expect_ReturnOrder() {
		// given as @Sql
		long orderIdThatMustSuccess = 1L;
		
		// when
		Order order = orderMapper.selectOrderForUpdate(orderIdThatMustSuccess);
		
		// then
		assertThat(order).isNotNull();
	}
	
	@Test
	@DisplayName("비관적 락을 걸고 주문을 조회시 주문이 존재하지 않으면 null을 반환하는지 테스트")
	void When_SelectOrderForUpdate_Expect_ReturnNull() {
		// given as @Sql
		long orderIdThatMustFail = 100L;
		
		// when
		Order order = orderMapper.selectOrderForUpdate(orderIdThatMustFail);
		
		// then
		assertThat(order).isNull();
	}
	
	@Test
	@DisplayName("주문을 조회시 주문이 존재하면 주문을 반환하는지 테스트")
	void When_SelectOrder_Expect_ReturnOrder() {
		// given as @Sql
		long orderIdThatMustSuccess = 1L;
		Order expectedOrder = Order.createOrder(
			1L,
			1L,
			OrderStatus.ACCEPT,
			LocalDateTime.now(),
			1L,
			LocalDateTime.now(),
			null,
			null,
			List.of(
				mock(OrdersProductRelation.class),
				mock(OrdersProductRelation.class)
			),
			mock(Member.class),
			mock(ShippingInfo.class)
		);
		
		// when
		Order order = orderMapper.selectOrder(OrderRequestDTO.forActiveOrder(orderIdThatMustSuccess));
		
		// then
		assertThat(order).isNotNull();
		assertThat(order).isEqualTo(expectedOrder);
	}
	
	@Test
	@DisplayName("주문을 조회시 주문이 존재하지 않으면 null을 반환하는지 테스트")
	void When_SelectOrder_Expect_ReturnNull() {
		// given as @Sql
		long orderIdThatMustFail = 100L;
		
		// when
		Order order = orderMapper.selectOrder(OrderRequestDTO.forActiveOrder(orderIdThatMustFail));
		
		// then
		assertThat(order).isNull();
	}
}