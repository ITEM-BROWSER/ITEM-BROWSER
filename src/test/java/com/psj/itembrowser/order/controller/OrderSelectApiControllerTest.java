package com.psj.itembrowser.order.controller;

import static com.psj.itembrowser.security.common.exception.ErrorCode.ORDER_NOT_FOUND;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.psj.itembrowser.member.annotation.MockMember;
import com.psj.itembrowser.member.domain.dto.response.MemberResponseDTO;
import com.psj.itembrowser.member.domain.vo.Address;
import com.psj.itembrowser.member.domain.vo.Credentials;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.member.domain.vo.MemberNo;
import com.psj.itembrowser.member.domain.vo.Name;
import com.psj.itembrowser.order.domain.dto.response.OrderResponseDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrderStatus;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelation;
import com.psj.itembrowser.order.service.OrderService;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.security.common.exception.NotFoundException;
import com.psj.itembrowser.security.service.impl.UserDetailsServiceImpl;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;
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

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(OrderApiController.class)
@AutoConfigureRestDocs
public class OrderSelectApiControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private OrderService orderService;
	
	@MockBean
	private UserDetailsServiceImpl userDetailsService;
	
	private Order expectedOrderWithADMINUser;
	private Order expectedOrderWithCUSTOMERUser;
	
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext,
		RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
			.build();
		
		Member expectedAdminMember = new Member(MemberNo.create(1L),
			Credentials.create("mockUser3410@gamil.com", "3410"),
			Name.create("홍", "길동"),
			"010-1234-1234",
			Member.Gender.MEN,
			Member.Role.ROLE_ADMIN, Member.Status.ACTIVE, Address.create("서울시 강남구", "김밥빌딩 101동 302호", "01012"),
			LocalDate.of(1995, 11, 3),
			LocalDateTime.now());
		
		Member expectedCustomerMember = new Member(MemberNo.create(1L),
			Credentials.create("mockUser3410@gmail.com", "3410"),
			Name.create("홍", "길동"),
			"010-1234-1234",
			Member.Gender.MEN,
			Member.Role.ROLE_CUSTOMER, Member.Status.ACTIVE, Address.create("서울시 강남구", "김밥빌딩 101동 302호", "01012"),
			LocalDate.of(1995, 11, 3),
			LocalDateTime.now());
		
		ShippingInfo expectedShppingInfo = new ShippingInfo(1L,
			"mockUser3410@gamil.com",
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
		
		this.expectedOrderWithADMINUser = Order.createOrder(
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
			expectedAdminMember,
			expectedShppingInfo);
		
		this.expectedOrderWithCUSTOMERUser = Order.createOrder(
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
			expectedCustomerMember,
			expectedShppingInfo);
	}
	
	@Test
	@MockMember
	@DisplayName("권한 - CUSTOMER 의 경우 주문 단건 조회시 200 성공과 올바른 응답값이 오는지 확인합니다.")
	void When_GetOrderWithCustomer_Expect_Status200() throws Exception {
		// given
		long orderId = 1L;
		OrderResponseDTO expectedOrderResponseDTO = OrderResponseDTO.create(expectedOrderWithCUSTOMERUser);
		given(orderService.getOrderWithNotDeleted(orderId)).willReturn(expectedOrderResponseDTO);
		given(userDetailsService.loadUserByJwt(any())).willReturn(
			new UserDetailsServiceImpl.CustomUserDetails(expectedOrderResponseDTO.getMember()));
		
		// when - then
		ResultActions response = mockMvc.perform(get("/v1/api/orders/{orderId}", orderId)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(expectedOrderResponseDTO.getId()))
			.andExpect(jsonPath("$.ordererNumber").value(expectedOrderResponseDTO.getOrdererNumber()))
			.andExpect(jsonPath("$.member.memberNo").value(expectedOrderResponseDTO.getMember().getMemberNo()))
			.andExpect(jsonPath("$.member.email").value(expectedOrderResponseDTO.getMember().getEmail()))
			.andExpect(jsonPath("$.member.role").value(Member.Role.ROLE_CUSTOMER.name()));
		
		response
			.andDo(MockMvcRestDocumentationWrapper.document(
				"get-order-customer",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				ResourceDocumentation.resource(ResourceSnippetParameters.builder()
					.tag("order")
					.summary("주문 단건 조회 API - CUSTOMER 권한의 경우")
					.pathParameters(
						parameterWithName("orderId").description("주문 ID")
					)
					.responseFields(
						fieldWithPath("id").description("주문 ID"),
						fieldWithPath("ordererNumber").description("주문자 번호"),
						fieldWithPath("orderStatus").description("주문 상태"),
						fieldWithPath("paidDate").description("결제 일자"),
						fieldWithPath("shippingInfoId").description("배송지 ID"),
						fieldWithPath("createdDate").description("생성 일자"),
						fieldWithPath("updatedDate").description("수정 일자"),
						fieldWithPath("deletedDate").description("삭제 일자"),
						fieldWithPath("member").description("주문자 정보"),
						fieldWithPath("member.memberNo").description("주문자 번호"),
						fieldWithPath("member.email").description("이메일"),
						fieldWithPath("member.firstName").description("이름"),
						fieldWithPath("member.lastName").description("성"),
						fieldWithPath("member.phoneNumber").description("전화번호"),
						fieldWithPath("member.addressMain").description("주소"),
						fieldWithPath("member.addressSub").description("상세주소"),
						fieldWithPath("member.zipCode").description("우편번호"),
						fieldWithPath("member.gender").description("성별"),
						fieldWithPath("member.role").description("역할"),
						fieldWithPath("member.status").description("상태"),
						fieldWithPath("member.birthday").description("생년월일"),
						fieldWithPath("member.lastLoginDate").description("마지막 로그인 일자"),
						fieldWithPath("member.createdDate").optional().description("생성 일자"),
						fieldWithPath("member.updatedDate").optional().description("수정 일자"),
						fieldWithPath("member.deletedDate").optional().description("삭제 일자")
					)
					.build()
				)
			));
	}
	
	@Test
	@DisplayName("권한 - ADMIN 의 경우 주문 단건 조회시 200 성공과 올바른 응답값이 오는지 확인합니다.")
	@MockMember(role = Member.Role.ROLE_ADMIN)
	void When_GetOrderWithAdmin_Expect_Status200() throws Exception {
		// given
		long orderId = 1L;
		OrderResponseDTO expectedOrderResponseDTO = OrderResponseDTO.create(expectedOrderWithADMINUser);
		given(orderService.getOrderWithNoCondition(orderId)).willReturn(expectedOrderResponseDTO);
		given(userDetailsService.loadUserByJwt(any())).willReturn(
			new UserDetailsServiceImpl.CustomUserDetails(expectedOrderResponseDTO.getMember()));
		
		// when - then
		ResultActions response = mockMvc.perform(get("/v1/api/orders/{orderId}", orderId)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(expectedOrderResponseDTO.getId()))
			.andExpect(jsonPath("$.ordererNumber").value(expectedOrderResponseDTO.getOrdererNumber()))
			.andExpect(jsonPath("$.member.memberNo").value(expectedOrderResponseDTO.getMember().getMemberNo()))
			.andExpect(jsonPath("$.member.email").value(expectedOrderResponseDTO.getMember().getEmail()))
			.andExpect(jsonPath("$.member.role").value(Member.Role.ROLE_ADMIN.name()));
		
		response
			.andDo(MockMvcRestDocumentationWrapper.document(
				"get-order-customer",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				ResourceDocumentation.resource(ResourceSnippetParameters.builder()
					.tag("order")
					.summary("주문 단건 조회 API - ADMIN 권한의 경우")
					.pathParameters(
						parameterWithName("orderId").description("주문 ID")
					)
					.responseFields(
						fieldWithPath("id").description("주문 ID"),
						fieldWithPath("ordererNumber").description("주문자 번호"),
						fieldWithPath("orderStatus").description("주문 상태"),
						fieldWithPath("paidDate").description("결제 일자"),
						fieldWithPath("shippingInfoId").description("배송지 ID"),
						fieldWithPath("createdDate").description("생성 일자"),
						fieldWithPath("updatedDate").description("수정 일자"),
						fieldWithPath("deletedDate").description("삭제 일자"),
						fieldWithPath("member").description("주문자 정보"),
						fieldWithPath("member.memberNo").description("주문자 번호"),
						fieldWithPath("member.email").description("이메일"),
						fieldWithPath("member.firstName").description("이름"),
						fieldWithPath("member.lastName").description("성"),
						fieldWithPath("member.phoneNumber").description("전화번호"),
						fieldWithPath("member.addressMain").description("주소"),
						fieldWithPath("member.addressSub").description("상세주소"),
						fieldWithPath("member.zipCode").description("우편번호"),
						fieldWithPath("member.gender").description("성별"),
						fieldWithPath("member.role").description("역할"),
						fieldWithPath("member.status").description("상태"),
						fieldWithPath("member.birthday").description("생년월일"),
						fieldWithPath("member.lastLoginDate").description("마지막 로그인 일자"),
						fieldWithPath("member.createdDate").optional().description("생성 일자"),
						fieldWithPath("member.updatedDate").optional().description("수정 일자"),
						fieldWithPath("member.deletedDate").optional().description("삭제 일자")
					)
					.build()
				)
			));
	}
	
	@Test
	@MockMember(role = Member.Role.ROLE_CUSTOMER)
	@DisplayName("권한 - CUSTOMER 인 경우, 주문 단건 조회 실패 케이스의 경우 404 반환과 올바른 응답값이 오는지 확인합니다.")
	void When_GetOrderWithCUSTOMER_Expect_Status404() throws Exception {
		// given
		long orderId = 1L;
		given(orderService.getOrderWithNotDeleted(orderId)).willThrow(new NotFoundException(ORDER_NOT_FOUND));
		given(userDetailsService.loadUserByJwt(any())).willReturn(
			new UserDetailsServiceImpl.CustomUserDetails(
				MemberResponseDTO.from(expectedOrderWithCUSTOMERUser.getMember())));
		
		// when - then
		ResultActions response = mockMvc.perform(get("/v1/api/orders/{orderId}", orderId)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value(ORDER_NOT_FOUND.getMessage()));
		
		response
			.andDo(MockMvcRestDocumentationWrapper.document(
				"get-order-fail-customer",
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
	
	@Test
	@MockMember(role = Member.Role.ROLE_ADMIN)
	@DisplayName("권한 - ADMIN 인 경우, 주문 단건 조회 실패 케이스의 경우 404 반환과 올바른 응답값이 오는지 확인합니다.")
	void When_GetOrderWithADMIN_Expect_Status404() throws Exception {
		// given
		long orderId = 1L;
		given(orderService.getOrderWithNoCondition(orderId)).willThrow(new NotFoundException(ORDER_NOT_FOUND));
		given(userDetailsService.loadUserByJwt(any())).willReturn(
			new UserDetailsServiceImpl.CustomUserDetails(
				MemberResponseDTO.from(expectedOrderWithADMINUser.getMember())));
		
		// when - then
		ResultActions response = mockMvc.perform(get("/v1/api/orders/{orderId}", orderId)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value(ORDER_NOT_FOUND.getMessage()));
		
		response
			.andDo(MockMvcRestDocumentationWrapper.document(
				"get-order-fail-admin",
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