package com.psj.itembrowser.order.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.psj.itembrowser.order.domain.vo.Order.PaymentStatus;
import com.psj.itembrowser.security.common.exception.BadRequestException;

public class OrderTest {

	private Order order;

	@BeforeEach
	public void setUp() {
		order = new Order();
	}

	@Test
	@DisplayName("결제 완료처리시 결제 상태가 COMPLETE 로 변경되며 결제일이 설정된다.")
	public void testCompletePaymentWhenPaymentStatusIsPendingThenPaymentStatusIsCompleteAndPaidDateIsNotNull() {
		// Given
		ReflectionTestUtils.setField(order, "paymentStatus", PaymentStatus.PENDING);
		ReflectionTestUtils.setField(order, "paidDate", null);

		// When
		order.completePayment();

		// Then
		assertThat(order.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETE);
		assertThat(order.getPaidDate()).isNotNull();
	}

	@Test
	@DisplayName("이미 결제완료된 주문을 결제완료 처리하는 경우 에러가 발생한다.")
	public void testCompletePaymentWhenPaymentStatusIsCompleteThenPaymentStatusIsStillCompleteAndPaidDateIsNull() {
		// Given
		ReflectionTestUtils.setField(order, "paymentStatus", PaymentStatus.COMPLETE);

		// When - Then
		assertThatThrownBy(() -> order.completePayment())
			.isInstanceOf(BadRequestException.class)
			.hasMessage("Already complete payment");
	}
}