package com.psj.itembrowser.order.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.persistence.OrderPersistence;
import com.psj.itembrowser.order.service.impl.OrderServiceImpl;
import com.psj.itembrowser.security.common.exception.BadRequestException;
import com.psj.itembrowser.security.common.exception.DatabaseOperationException;
import com.psj.itembrowser.security.common.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
public class OrderDeleteServiceTest {

	@InjectMocks
	private OrderServiceImpl orderService;

	@Mock
	private OrderPersistence orderPersistence;

	@Test
	@DisplayName("주문 삭제 성공에 대해 주문에 대한 상품들도 취소되어야함 - exception이 발생하지 않아야 합니다.")
	void When_DeleteOrder_Expect_DonotThrowException_VerifyMethodCalls() {
		// given as @Mock
		long orderIdThatMustSuccess = 1L;
		Order mockOrder = mock(Order.class);
		ReflectionTestUtils.setField(mockOrder, "orderStatus", Order.OrderStatus.INSTRUCT);

		given(orderPersistence.findOrderStatusForUpdate(orderIdThatMustSuccess)).willReturn(mockOrder);

		// when
		ThrowingCallable throwingCallable = () -> orderService.removeOrder(orderIdThatMustSuccess);

		// then
		assertThatCode(throwingCallable).as("주문 삭제에 성공하는 경우 어떤 에러도 던져지지 않아야 합니다.")
			.doesNotThrowAnyException();

		verify(orderPersistence).removeOrder(orderIdThatMustSuccess);
		verify(orderPersistence).removeOrderProducts(orderIdThatMustSuccess);
	}

	@Test
	@DisplayName("주문 삭제 실패가 발생시 주문 삭제에 예외가 발생되는지 체크해야합니다.")
	void When_DeleteOrder_Expect_VerifyMethodCalls() {
		// given as @Mock
		long orderIdThatMustFail = 1L;
		Order mockOrder = mock(Order.class);
		ReflectionTestUtils.setField(mockOrder, "orderStatus", Order.OrderStatus.INSTRUCT);

		given(orderPersistence.findOrderStatusForUpdate(orderIdThatMustFail)).willReturn(mockOrder);
		willThrow(new DatabaseOperationException(ErrorCode.ORDER_DELETE_FAIL)).given(orderPersistence)
			.removeOrder(orderIdThatMustFail);
		// when
		ThrowingCallable throwingCallable = () -> orderService.removeOrder(orderIdThatMustFail);

		// then
		assertThatCode(throwingCallable).as("주문 삭제에 실패하는 경우 DatabaseOperationException 이 발생해야 합니다.")
			.isInstanceOf(DatabaseOperationException.class)
			.hasMessage(ErrorCode.ORDER_DELETE_FAIL.getMessage());

		verify(orderPersistence).removeOrder(orderIdThatMustFail);
		verify(orderPersistence, times(0)).removeOrderProducts(orderIdThatMustFail);
	}

	@Test
	@DisplayName("주문 - 상품 삭제시 예외가 발생하는 경우 테스트 -> 주문 삭제는 메서드가 수행되어야 한다.")
	void When_DeleteOrderProducts_Expect_VerifyMethodCalls() {
		// given as @Mock
		long orderIdThatMustFail = 1L;
		Order mockOrder = mock(Order.class);
		ReflectionTestUtils.setField(mockOrder, "orderStatus", Order.OrderStatus.INSTRUCT);

		given(orderPersistence.findOrderStatusForUpdate(orderIdThatMustFail)).willReturn(mockOrder);
		doThrow(new DatabaseOperationException(ErrorCode.ORDER_RELATION_DELETE_FAIL)).when(orderPersistence)
			.removeOrderProducts(orderIdThatMustFail);

		// when
		ThrowingCallable throwingCallable = () -> orderService.removeOrder(orderIdThatMustFail);

		// then
		assertThatCode(throwingCallable).as("주문 -상품 삭제에 실패하는 경우 DatabaseOperationException 이 발생해야 합니다.")
			.isInstanceOf(DatabaseOperationException.class)
			.hasMessage(ErrorCode.ORDER_RELATION_DELETE_FAIL.getMessage());

		verify(orderPersistence).removeOrder(orderIdThatMustFail);
		verify(orderPersistence).removeOrderProducts(orderIdThatMustFail);
	}

	@Test
	@DisplayName("주문 상태가 결제완료 , 상품준비중인 경우에는 취소가 가능해야합니다.")
	void When_OrderStatusIsInstructAndAccept_Expect_OrderCanCancel() {
		// given as @Mock
		long orderIdThatMustFail = 1L;
		int count = 0;
		Set<Order.OrderStatus> orderStatuses = EnumSet.of(Order.OrderStatus.INSTRUCT, Order.OrderStatus.ACCEPT);

		for (Order.OrderStatus ordeStatusCanCanceled : orderStatuses) {
			count++;
			Order mockOrder = mock(Order.class);
			ReflectionTestUtils.setField(mockOrder, "orderStatus", Order.OrderStatus.INSTRUCT);

			given(orderPersistence.findOrderStatusForUpdate(orderIdThatMustFail)).willReturn(mockOrder);

			// when
			orderService.removeOrder(orderIdThatMustFail);

			// then
			verify(orderPersistence, times(count)).removeOrder(orderIdThatMustFail);
			verify(orderPersistence, times(count)).removeOrderProducts(orderIdThatMustFail);
		}
	}

	@Test
	@DisplayName("주문 상태가 취소되지 않아야할 상황에는 BadRequestException 이 발생해야합니다.")
	void When_OrderStatusIsNotInstructAndAccept_Expect_ThrowBadRequestException() {
		// given as @Mock
		long orderIdThatMustFail = 1L;
		Set<Order.OrderStatus> orderStatuses = EnumSet.allOf(Order.OrderStatus.class)
			.stream()
			.filter(status -> status != Order.OrderStatus.INSTRUCT && status != Order.OrderStatus.ACCEPT)
			.collect(Collectors.toCollection(LinkedHashSet::new));

		for (Order.OrderStatus orderStatusCanCanceled : orderStatuses) {
			Order mockOrder = mock(Order.class);
			ReflectionTestUtils.setField(mockOrder, "orderStatus", orderStatusCanCanceled);

			given(orderPersistence.findOrderStatusForUpdate(orderIdThatMustFail)).willReturn(mockOrder);
			given(mockOrder.isNotCancelable()).willReturn(true);

			// when
			ThrowingCallable throwingCallable = () -> orderService.removeOrder(orderIdThatMustFail);

			// then
			assertThatCode(throwingCallable).as("When the order status should not be cancellable, a BadRequestException should be thrown.")
				.isInstanceOf(BadRequestException.class)
				.hasMessage(ErrorCode.ORDER_NOT_CANCELABLE.getMessage());

			verify(orderPersistence, times(0)).removeOrder(orderIdThatMustFail);
			verify(orderPersistence, times(0)).removeOrderProducts(orderIdThatMustFail);
		}
	}
}