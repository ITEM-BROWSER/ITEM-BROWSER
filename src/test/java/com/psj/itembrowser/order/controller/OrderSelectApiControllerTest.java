package com.psj.itembrowser.order.controller;

import static com.psj.itembrowser.common.exception.ErrorCode.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.psj.itembrowser.common.exception.NotFoundException;
import com.psj.itembrowser.member.domain.vo.Address;
import com.psj.itembrowser.member.domain.vo.Credentials;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.member.domain.vo.MemberNo;
import com.psj.itembrowser.member.domain.vo.Name;
import com.psj.itembrowser.order.domain.dto.OrderResponseDTO;
import com.psj.itembrowser.order.domain.dto.request.OrderRequestDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrderStatus;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelation;
import com.psj.itembrowser.order.service.OrderService;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(OrderApiController.class)
@AutoConfigureRestDocs
public class OrderSelectApiControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private OrderService orderService;
	
	private Order expectedOrder;
	
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext,
		RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
			.build();
		
		Member expectedMember = new Member(MemberNo.create(1L), Credentials.create("test@test.com", "test"),
			Name.create("홍", "길동"),
			"010-1234-1234",
			Member.Gender.MEN,
			Member.Role.ROLE_CUSTOMER, Member.Status.ACTIVE, Address.create("서울시 강남구", "김밥빌딩 101동 302호", "01012"),
			LocalDate.of(1995, 11, 3),
			LocalDateTime.now());
		
		ShippingInfo expectedShppingInfo = new ShippingInfo(1L,
			"test@test.com",
			"홍길동",
			"test",
			"test", "010-1235-1234",
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
		
		this.expectedOrder = Order.createOrder(
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
	@DisplayName("주문 단건 조회 성공 케이스의 경우 200 반환과 올바른 응답값이 오는지 확인합니다.")
	void When_GetOrder_Expect_Status200() throws Exception {
		// given
		long orderId = 1L;
		OrderRequestDTO orderRequestDTO = OrderRequestDTO.forActiveOrder(orderId);
		OrderResponseDTO expectedOrderResponseDTO = OrderResponseDTO.fromOrder(expectedOrder);
		given(orderService.getOrder(orderRequestDTO)).willReturn(expectedOrderResponseDTO);
		
		// when - then
		ResultActions response = mockMvc.perform(get("/v1/api/orders/{orderId}", orderId)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(expectedOrderResponseDTO.getId()))
			.andExpect(jsonPath("$.ordererId").value(expectedOrderResponseDTO.getOrdererId()));
		
		response
			.andDo(MockMvcRestDocumentationWrapper.document(
				"get-order",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				ResourceDocumentation.resource(ResourceSnippetParameters.builder()
					.tag("order")
					.summary("주문 단건 조회 API")
					.pathParameters(
						parameterWithName("orderId").description("주문 ID")
					)
					.responseFields(
						fieldWithPath("id").description("주문 ID"),
						fieldWithPath("ordererId").description("주문자 ID"),
						fieldWithPath("orderStatus").description("주문 상태"),
						fieldWithPath("paidDate").description("결제 일자"),
						fieldWithPath("shippingInfoId").description("배송지 ID"),
						fieldWithPath("createdDate").description("생성 일자"),
						fieldWithPath("updatedDate").description("수정 일자"),
						fieldWithPath("deletedDate").description("삭제 일자")
					).build()
				)
			));
	}
	
	@Test
	@DisplayName("주문 단건 조회 실패 케이스의 경우 404 반환과 올바른 응답값이 오는지 확인합니다.")
	void When_GetOrder_Expect_Status404() throws Exception {
		// given
		long orderId = 1L;
		OrderRequestDTO orderRequestDTO = OrderRequestDTO.forActiveOrder(orderId);
		given(orderService.getOrder(orderRequestDTO)).willThrow(new NotFoundException(ORDER_NOT_FOUND));
		
		// when - then
		ResultActions response = mockMvc.perform(get("/v1/api/orders/{orderId}", orderId)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value(ORDER_NOT_FOUND.getMessage()));
		
		response
			.andDo(MockMvcRestDocumentationWrapper.document(
				"get-order-fail",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				ResourceDocumentation.resource(ResourceSnippetParameters.builder()
					.tag("order")
					.summary("주문 단건 조회 실패 API")
					.pathParameters(
						parameterWithName("orderId").description("주문 ID")
					)
					.responseFields(
						fieldWithPath("status").description("오류 상태 코드"),
						fieldWithPath("error").description("에러 메시지"),
						fieldWithPath("message").description("오류 메시지"),
						fieldWithPath("path").description("URI"),
						fieldWithPath("timestamp").description("시간")
					).build()
				)
			));
	}
}