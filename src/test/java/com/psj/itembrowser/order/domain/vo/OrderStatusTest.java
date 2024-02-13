package com.psj.itembrowser.order.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * packageName    : com.psj.itembrowser.order.domain.vo
 * fileName       : OrderStatusTest
 * author         : ipeac
 * date           : 2023-12-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-12-11        ipeac       최초 생성
 */
@ExtendWith(MockitoExtension.class)
public class OrderStatusTest {

	@Test
	@DisplayName("주문 상태 검색시 정상적으로 검색되어야 합니다.")
	void When_ofMethodCalled_Expect_OrderStatusReturned() {
		// given
		String orderStatusName = Order.OrderStatus.CANCELED.getValue();

		// when
		Order.OrderStatus orderStatus = Order.OrderStatus.of(orderStatusName);

		// then
		assertThat(orderStatus).isNotNull();

		assertThat(orderStatus).isEqualTo(Order.OrderStatus.CANCELED);
	}

	@Test
	@DisplayName("주문 상태 검색시 존재하지 않는 주문 상태명이면 IllegalArgumentException이 발생해야 합니다.")
	void When_ofMethodCalledWithInvalidOrderStatusName_Expect_IllegalArgumentExceptionThrown() {
		// given
		String orderStatusName = "INVALID";

		// when & then
		assertThatThrownBy(() -> Order.OrderStatus.of(orderStatusName))
			.as("주문 상태가 존재하지 않을 때 IllegalArgumentException이 발생해야 합니다.")
			.isInstanceOf(IllegalArgumentException.class);
	}
}