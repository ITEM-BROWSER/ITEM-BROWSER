package com.psj.itembrowser.order.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.psj.itembrowser.member.domain.vo.Address;
import com.psj.itembrowser.member.domain.vo.Credentials;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.member.domain.vo.MemberNo;
import com.psj.itembrowser.member.domain.vo.Name;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrderStatus;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelation;
import com.psj.itembrowser.order.mapper.OrderMapper;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.security.common.exception.NotFoundException;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;

/**
 * packageName    : com.psj.itembrowser.order.persistence
 * fileName       : OrderPersistenceTest
 * author         : ipeac
 * date           : 2023-11-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-08        ipeac       최초 생성
 */
@ExtendWith(MockitoExtension.class)
public class OrderSelectPersistenceTest {
	@InjectMocks
	private OrderPersistence orderPersistence;

	@Mock
	private OrderMapper orderMapper;

	private Order expectedOrder;

	@BeforeEach
	void setUp() {
		Member expectedMember = new Member(MemberNo.create(1L), Credentials.create("test@test.com", "test"),
			Name.create("홍", "길동"),
			"010-1234-1234",
			Member.Gender.MEN,
			Member.Role.ROLE_CUSTOMER,
			Member.Status.ACTIVE,
			Member.MemberShipType.REGULAR,
			Address.create("서울시 강남구", "김밥빌딩 101동 302호", "01012"),
			LocalDate.of(1995, 11, 3),
			LocalDateTime.now());

		ShippingInfo expectedShppingInfo = new ShippingInfo(1L,
			"test@test.com",
			"홍길동",
			"test",
			"test",
			"010-1235-1234",
			"01111",
			"010-1234-1234", "test",
			LocalDateTime.now(),
			null,
			null);

		OrdersProductRelation expectedOrderRelation1 = OrdersProductRelation.create(1L, 1L, 1, LocalDateTime.now(),
			null,
			null,
			new Product());

		OrdersProductRelation expectedOrderRelation2 = OrdersProductRelation.create(2L, 1L, 1, LocalDateTime.now(),
			null,
			null, new Product());

		this.expectedOrder = Order.of(
			1L,
			1L,
			OrderStatus.ACCEPT,
			LocalDateTime.now(),
			1L,
			LocalDateTime.now(),
			null,
			null,
			List.of(
				expectedOrderRelation1,
				expectedOrderRelation2
			),
			expectedMember,
			expectedShppingInfo);
	}

	@Test
	@DisplayName("주문에 대한 단건 조회의 경우 메서드가 정상적으로 Order 를 반환하는지")
	void When_GetOrder_Expect_ReturnOrder() {
		//given
		given(orderMapper.selectOrderWithNotDeleted(1L)).willReturn(expectedOrder);

		//when
		Order foundOrder = orderPersistence.getOrderWithNotDeleted(1L);

		//then
		assertThat(foundOrder).isNotNull();
		assertThat(foundOrder).isEqualTo(expectedOrder);
	}

	@Test
	@DisplayName("주문에 대한 단건 조회의 경우 메서드가 정상적으로 NotFoundException 을 반환하는지")
	void When_GetOrder_Expect_ThrowNotFoundException() {
		//given
		given(orderMapper.selectOrderWithNotDeleted(1L)).willReturn(null);

		//when
		//then
		assertThatThrownBy(() -> orderPersistence.getOrderWithNotDeleted(1L))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining("Not Found Order");
	}
}