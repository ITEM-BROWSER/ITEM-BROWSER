package com.psj.itembrowser.order.mapper;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

import com.github.pagehelper.page.PageMethod;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.domain.dto.request.OrderPageRequestDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrderStatus;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelation;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;

@MybatisTest
@AutoConfigureTestDatabase(replace = Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
@Sql(scripts = {"classpath:drop-table.sql", "classpath:schema.sql",
	"classpath:sql/h2/member/insert_member.sql",
	"classpath:sql/h2/shippinginfo/insert_shipping_info.sql",
	"classpath:sql/h2/product/insert_product.sql",
	"classpath:sql/h2/order/insert_order_product.sql",
	"classpath:sql/h2/order/insert_order.sql"})
public class OrderSelectMapperTest {

	@Autowired
	private OrderMapper orderMapper;

	@Test
	@DisplayName("1번 주문을 조회시 기대한 1번 주문과 같은 주문이 조회되는지 테스트")
	void When_SelectOrder_Expect_Order1() {
		// given as @Sql
		long expectedId = 1L;
		Order expectedOrder = Order.of(
			expectedId,
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
		Order order = orderMapper.selectOrderWithNotDeleted(expectedId);

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
		Order order = orderMapper.selectOrderWithPessimissticLock(orderIdThatMustSuccess);

		// then
		assertThat(order).isNotNull();
	}

	@Test
	@DisplayName("비관적 락을 걸고 주문을 조회시 주문이 존재하지 않으면 null을 반환하는지 테스트")
	void When_SelectOrderForUpdate_Expect_ReturnNull() {
		// given as @Sql
		long orderIdThatMustFail = 100L;

		// when
		Order order = orderMapper.selectOrderWithPessimissticLock(orderIdThatMustFail);

		// then
		assertThat(order).isNull();
	}

	@Test
	@DisplayName("삭제되지 않은 주문을 조회시 주문이 존재하면 주문을 반환하는지 테스트")
	void When_selectOrderWithNotDeleted_Expect_ReturnOrder() {
		// given as @Sql
		long orderIdThatMustSuccess = 1L;
		Order expectedOrder = Order.of(
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
		Order order = orderMapper.selectOrderWithNotDeleted(orderIdThatMustSuccess);

		// then
		assertThat(order).isNotNull();
		assertThat(order).isEqualTo(expectedOrder);
	}

	@Test
	@DisplayName("삭제되지 않은 주문을 조회시 주문이 존재하지 않으면 null을 반환하는지 테스트")
	void When_SelectOrder_Expect_ReturnNull() {
		// given as @Sql
		long orderIdThatMustFail = 100L;

		// when
		Order order = orderMapper.selectOrderWithNotDeleted(orderIdThatMustFail);

		// then
		assertThat(order).isNull();
	}

	@Test
	@DisplayName("조건 없이 주문을 조회시 주문이 존재하면 주문을 반환하는지 테스트")
	void When_SelectOrderWithNoCondition_Expect_ReturnOrder() {
		// given as @Sql
		long orderIdThatMustSuccess = 1L;
		Order expectedOrder = Order.of(
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
		Order order = orderMapper.selectOrderWithNoCondition(orderIdThatMustSuccess);

		// then
		assertThat(order).isNotNull();
		assertThat(order).isEqualTo(expectedOrder);
	}

	@Test
	@DisplayName("조건 없이 주문을 조회시 주문이 존재하지 않으면 null을 반환하는지 테스트")
	void When_SelectOrderWithNoCondition_Expect_ReturnNull() {
		// given as @Sql
		long orderIdThatMustFail = 100L;

		// when
		Order order = orderMapper.selectOrderWithNoCondition(orderIdThatMustFail);

		// then
		assertThat(order).isNull();
	}

	@Test
	@DisplayName("주문 다건 조회(selectOrdersWithPaginationAndNoCondition) -> 정상 조회 테스트")
	void When_SelectOrdersWithPaginationAndNoCondition_Expect_ReturnOrdersWithPagination() {
		// given as @Sql
		int pageNum = 0;
		int pageSize = 10;

		Order expectedOrder = Order.of(
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

		PageMethod.startPage(pageNum, pageSize);

		OrderPageRequestDTO requestDTO = mock(OrderPageRequestDTO.class);
		given(requestDTO.getPageNum()).willReturn(pageNum);
		given(requestDTO.getPageSize()).willReturn(pageSize);

		// when
		List<Order> orders = orderMapper.selectOrdersWithPaginationAndNoCondition(requestDTO);

		// then
		assertThat(orders).isNotNull();
		assertThat(orders).hasSize(2);
		assertThat(orders.get(0).getOrdererNumber()).isEqualTo(expectedOrder.getOrdererNumber());
		assertThat(orders.get(0).getOrderStatus()).isEqualTo(expectedOrder.getOrderStatus());
		assertThat(orders.get(0).getShippingInfoId()).isEqualTo(expectedOrder.getShippingInfoId());
	}

	@Test
	@DisplayName("주문 다건 조회(selectOrdersWithPaginationAndNoCondition) -> 조회된 주문이 없을 때 테스트 => 페이징 X 단순 DB 연결만 테스트")
	void When_SelectOrdersWithPaginationAndNoCondition_Expect_ReturnEmptyList() {
		// given as @Sql
		int pageNum = 100;
		int pageSize = 10;

		PageMethod.startPage(pageNum, pageSize);

		OrderPageRequestDTO requestDTO = mock(OrderPageRequestDTO.class);
		given(requestDTO.getPageNum()).willReturn(pageNum);
		given(requestDTO.getPageSize()).willReturn(pageSize);

		// when
		List<Order> orders = orderMapper.selectOrdersWithPaginationAndNoCondition(requestDTO);

		// then
		assertThat(orders).isNotNull();
	}

	@Test
	@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
	@Sql(statements = "UPDATE ORDERS SET DELETED_DATE = NOW() WHERE ID = 1")
	@DisplayName("주문 다건 조회(selectOrdersWithPaginationAndNotDeleted) -> 삭제되지 않은 1건 조회 정상 조회")
	void When_SelectOrdersWithPaginationAndNotDeleted_Expect_ReturnOrdersWithPaginationAndReturnTwoOrders() {
		// given as @Sql
		int pageNum = 0;
		int pageSize = 10;

		OrderPageRequestDTO requestDTO = mock(OrderPageRequestDTO.class);
		given(requestDTO.getPageNum()).willReturn(pageNum);
		given(requestDTO.getPageSize()).willReturn(pageSize);

		// when
		List<Order> orders = orderMapper.selectOrdersWithPaginationAndNotDeleted(requestDTO);

		// then
		assertThat(orders).isNotNull();
		assertThat(orders.size()).isEqualTo(1);
	}

	@Test
	@Sql(statements = "UPDATE ORDERS SET DELETED_DATE = NOW() WHERE ID = 1")
	@DisplayName("주문 다건 조회(selectOrdersWithPaginationAndNotDeleted) -> 삭제되지 않은 1건 조회시 조회된 주문이 없을 때 테스트")
	void When_SelectOrdersWithPaginationAndNotDeleted_Expect_ReturnEmptyList() {
		// given as @Sql
		int pageNum = 100;
		int pageSize = 10;

		PageMethod.startPage(pageNum, pageSize);

		OrderPageRequestDTO requestDTO = mock(OrderPageRequestDTO.class);
		given(requestDTO.getPageNum()).willReturn(pageNum);
		given(requestDTO.getPageSize()).willReturn(pageSize);

		// when
		List<Order> orders = orderMapper.selectOrdersWithPaginationAndNotDeleted(requestDTO);

		// then
		assertThat(orders).isNotNull();
		assertThat(orders).isEmpty();
	}
}