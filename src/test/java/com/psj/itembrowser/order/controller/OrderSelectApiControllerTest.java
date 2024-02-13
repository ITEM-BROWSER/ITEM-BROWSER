package com.psj.itembrowser.order.controller;

import static com.psj.itembrowser.security.common.exception.ErrorCode.*;
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
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
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
import com.github.pagehelper.PageInfo;
import com.psj.itembrowser.member.annotation.MockMember;
import com.psj.itembrowser.member.domain.dto.response.MemberResponseDTO;
import com.psj.itembrowser.member.domain.vo.Address;
import com.psj.itembrowser.member.domain.vo.Credentials;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.member.domain.vo.Member.Role;
import com.psj.itembrowser.member.domain.vo.MemberNo;
import com.psj.itembrowser.member.domain.vo.Name;
import com.psj.itembrowser.order.domain.dto.request.OrderPageRequestDTO;
import com.psj.itembrowser.order.domain.dto.response.OrderResponseDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrderStatus;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelation;
import com.psj.itembrowser.order.service.OrderService;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.security.common.exception.NotFoundException;
import com.psj.itembrowser.security.common.pagination.PageRequestDTO;
import com.psj.itembrowser.security.service.impl.UserDetailsServiceImpl;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;

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

	@Mock
	private Jwt jwt;

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
			Member.Role.ROLE_ADMIN, Member.Status.ACTIVE,
			Member.MemberShipType.REGULAR,
			Address.create("서울시 강남구", "김밥빌딩 101동 302호", "01012"),
			LocalDate.of(1995, 11, 3),
			LocalDateTime.now());

		Member expectedCustomerMember = new Member(MemberNo.create(1L),
			Credentials.create("mockUser3410@gmail.com", "3410"),
			Name.create("홍", "길동"),
			"010-1234-1234",
			Member.Gender.MEN,
			Member.Role.ROLE_CUSTOMER, Member.Status.ACTIVE,
			Member.MemberShipType.REGULAR,
			Address.create("서울시 강남구", "김밥빌딩 101동 302호", "01012"),
			LocalDate.of(1995, 11, 3),
			LocalDateTime.now());

		ShippingInfo expectedShppingInfo = new ShippingInfo(1L,
			"mockUser3410@gamil.com",
			"홍길동",
			"test",
			"test",
			"01111",
			"010-1235-1234",
			"010-1234-1234", "test",
			LocalDateTime.now(),
			null,
			null);

		OrdersProductRelation expectedOrderRelation1 = OrdersProductRelation.create(1L, 1L, 1,
			LocalDateTime.now(),
			null,
			null,
			new Product());

		OrdersProductRelation expectedOrderRelation2 = OrdersProductRelation.create(2L, 1L, 1,
			LocalDateTime.now(),
			null,
			null, new Product());

		this.expectedOrderWithADMINUser = Order.of(
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

		this.expectedOrderWithCUSTOMERUser = Order.of(
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
			.andExpect(
				jsonPath("$.ordererNumber").value(expectedOrderResponseDTO.getOrdererNumber()))
			.andExpect(jsonPath("$.member.memberNo").value(expectedOrderResponseDTO.getMember().getMemberNo()))
			.andExpect(
				jsonPath("$.member.email").value(expectedOrderResponseDTO.getMember().getEmail()))
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
						fieldWithPath("member.deletedDate").optional().description("삭제 일자"),
						fieldWithPath("ordersProductRelations").description("주문-상품 관계 리스트"),
						fieldWithPath("ordersProductRelations[].groupId").description("주문 ID"),
						fieldWithPath("ordersProductRelations[].productId").description("상품 ID"),
						fieldWithPath("ordersProductRelations[].productQuantity").description(
							"상품 수량"),
						fieldWithPath("ordersProductRelations[].createdDate").description("생성 일자"),
						fieldWithPath("ordersProductRelations[].updatedDate").description("수정 일자"),
						fieldWithPath("ordersProductRelations[].deletedDate").description("삭제 일자")
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
			.andExpect(
				jsonPath("$.ordererNumber").value(expectedOrderResponseDTO.getOrdererNumber()))
			.andExpect(jsonPath("$.member.memberNo").value(
				expectedOrderResponseDTO.getMember().getMemberNo()))
			.andExpect(
				jsonPath("$.member.email").value(expectedOrderResponseDTO.getMember().getEmail()))
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
						fieldWithPath("member.deletedDate").optional().description("삭제 일자"),
						fieldWithPath("ordersProductRelations").description("주문-상품 관계 리스트"),
						fieldWithPath("ordersProductRelations[].groupId").description("주문 ID"),
						fieldWithPath("ordersProductRelations[].productId").description("상품 ID"),
						fieldWithPath("ordersProductRelations[].productQuantity").description(
							"상품 수량"),
						fieldWithPath("ordersProductRelations[].createdDate").description("생성 일자"),
						fieldWithPath("ordersProductRelations[].updatedDate").description("수정 일자"),
						fieldWithPath("ordersProductRelations[].deletedDate").description("삭제 일자")
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
		given(userDetailsService.loadUserByJwt(any())).willReturn(new UserDetailsServiceImpl.CustomUserDetails(
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

	//다건 조회 테스트
	@Test
	@MockMember(role = Member.Role.ROLE_CUSTOMER)
	@DisplayName("권한 - CUSTOMER 인 경우, 삭제되지 않은 주문을 조회하는 서비스를 호출 시 200 성공을 기대합니다.")
	void When_GetOrdersWithCustomer_Expect_Status200()
		throws Exception {
		// given
		long userNumber = 1L;
		int pageNum = 0;
		int pageSize = 10;
		String requestYear = "2024";

		OrderResponseDTO expectedOrderResponseDTO = OrderResponseDTO.create(expectedOrderWithCUSTOMERUser);

		Member member = Member.from(MemberResponseDTO.from(expectedOrderWithCUSTOMERUser.getMember()));

		OrderPageRequestDTO orderPageRequestDTO = OrderPageRequestDTO.create(PageRequestDTO.create(pageNum, pageSize),
			1L, requestYear);

		PageInfo<OrderResponseDTO> expectedOrderResponseDTOPageInfo = new PageInfo<>(List.of(expectedOrderResponseDTO));

		given(orderService.getOrdersWithPaginationAndNotDeleted(member, orderPageRequestDTO)).willReturn(
			expectedOrderResponseDTOPageInfo);

		given(userDetailsService.loadUserByJwt(any())).willReturn(
			new UserDetailsServiceImpl.CustomUserDetails(expectedOrderResponseDTO.getMember()));

		// when - then
		ResultActions response = mockMvc.perform(
				get("/v1/api/orders/users/{userNumber}", userNumber)
					.param("pageNum", String.valueOf(pageNum))
					.param("pageSize", String.valueOf(pageSize))
					.param("requestYear", requestYear)
					.param("userNumber", String.valueOf(userNumber))
					.contentType(APPLICATION_JSON)
					.accept(APPLICATION_JSON))
			.andExpect(status().isOk());
		response
			.andDo(MockMvcRestDocumentationWrapper.document(
				"get-orders-customer",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				ResourceDocumentation.resource(ResourceSnippetParameters.builder()
					.tag("order")
					.summary("주문 다건 조회 API - CUSTOMER 권한의 경우")
					.pathParameters(
						parameterWithName("userNumber").description("사용자 번호")
					)
					.responseFields(
						fieldWithPath("total").description("전체 주문 수"),
						fieldWithPath("list").description("주문 리스트"),
						fieldWithPath("list[].id").description("주문 ID"),
						fieldWithPath("list[].ordererNumber").description("주문자 번호"),
						fieldWithPath("list[].orderStatus").description("주문 상태"),
						fieldWithPath("list[].paidDate").description("결제 일자"),
						fieldWithPath("list[].shippingInfoId").description("배송지 ID"),
						fieldWithPath("list[].createdDate").description("생성 일자"),
						fieldWithPath("list[].updatedDate").description("수정 일자"),
						fieldWithPath("list[].deletedDate").description("삭제 일자"),
						fieldWithPath("list[].member").description("주문자 정보"),
						fieldWithPath("list[].member.memberNo").description("주문자 번호"),
						fieldWithPath("list[].member.email").description("이메일"),
						fieldWithPath("list[].member.firstName").description("이름"),
						fieldWithPath("list[].member.lastName").description("성"),
						fieldWithPath("list[].member.phoneNumber").description("전화번호"),
						fieldWithPath("list[].member.addressMain").description("주소"),
						fieldWithPath("list[].member.addressSub").description("상세주소"),
						fieldWithPath("list[].member.zipCode").description("우편번호"),
						fieldWithPath("list[].member.gender").description("성별"),
						fieldWithPath("list[].member.role").description("역할"),
						fieldWithPath("list[].member.status").description("상태"),
						fieldWithPath("list[].member.birthday").description("생년월일"),
						fieldWithPath("list[].member.lastLoginDate").description("마지막 로그인 일자"),
						fieldWithPath("list[].member.createdDate").optional().description("생성 일자"),
						fieldWithPath("list[].member.updatedDate").optional().description("수정 일자"),
						fieldWithPath("list[].member.deletedDate").optional().description("삭제 일자"),
						fieldWithPath("list[].ordersProductRelations").description("주문-상품 관계 리스트"),
						fieldWithPath("list[].ordersProductRelations[].groupId").description("주문 ID"),
						fieldWithPath("list[].ordersProductRelations[].productId").description("상품 ID"),
						fieldWithPath("list[].ordersProductRelations[].productQuantity").description("상품 수량"),
						fieldWithPath("list[].ordersProductRelations[].createdDate").description("생성 일자"),
						fieldWithPath("list[].ordersProductRelations[].updatedDate").description("수정 일자"),
						fieldWithPath("list[].ordersProductRelations[].deletedDate").description("삭제 일자"),
						fieldWithPath("pageNum").description("현재 페이지 번호"),
						fieldWithPath("pageSize").description("페이지 크기"),
						fieldWithPath("size").description("현재 페이지 크기"),
						fieldWithPath("startRow").description("현재 페이지 시작 행"),
						fieldWithPath("endRow").description("현재 페이지 끝 행"),
						fieldWithPath("pages").description("전체 페이지 수"),
						fieldWithPath("prePage").description("이전 페이지"),
						fieldWithPath("nextPage").description("다음 페이지"),
						fieldWithPath("isFirstPage").description("첫 페이지 여부"),
						fieldWithPath("isLastPage").description("마지막 페이지 여부"),
						fieldWithPath("hasPreviousPage").description("이전 페이지 여부"),
						fieldWithPath("hasNextPage").description("다음 페이지 여부"),
						fieldWithPath("navigatePages").description("네비게이션 페이지 수"),
						fieldWithPath("navigatepageNums").description("네비게이션 페이지 번호"),
						fieldWithPath("navigateFirstPage").description("첫 네비게이션 페이지"),
						fieldWithPath("navigateLastPage").description("마지막 네비게이션 페이지")
					).build())));
	}

	// 404 NOT_FOUND_EXCEPTION 이 터지는 경우 테스트
	@Test
	@MockMember(role = Role.ROLE_CUSTOMER)
	@DisplayName("권한 - CUSTOMER 인 경우, 주문 다건 조회 실패 케이스의 경우 404 반환과 올바른 응답값이 오는지 확인합니다.")
	void When_GetOrdersWithCustomer_Pagination_SpecificYear_Expect_Status404() throws Exception {
		// given
		long userNumber = 1L;
		int pageNum = 100;
		int pageSize = 0;
		String requestYear = "2024";

		Member member = Member.from(MemberResponseDTO.from(expectedOrderWithCUSTOMERUser.getMember()));

		OrderPageRequestDTO orderPageRequestDTO = OrderPageRequestDTO.create(PageRequestDTO.create(pageNum, pageSize),
			1L, requestYear);

		given(orderService.getOrdersWithPaginationAndNotDeleted(member, orderPageRequestDTO)).willThrow(
			new NotFoundException(ORDER_NOT_FOUND));

		given(userDetailsService.loadUserByJwt(any())).willReturn(
			new UserDetailsServiceImpl.CustomUserDetails(
				MemberResponseDTO.from(expectedOrderWithCUSTOMERUser.getMember())));

		// when - then
		ResultActions response = mockMvc.perform(
				get("/v1/api/orders/users/{userNumber}", userNumber)
					.param("pageNum", String.valueOf(pageNum))
					.param("pageSize", String.valueOf(pageSize))
					.param("requestYear", requestYear)
					.param("userNumber", String.valueOf(userNumber))
					.contentType(APPLICATION_JSON)
					.accept(APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value(ORDER_NOT_FOUND.getMessage()));

		response
			.andDo(MockMvcRestDocumentationWrapper.document(
				"get-orders-fail-customer",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				ResourceDocumentation.resource(ResourceSnippetParameters.builder()
					.tag("order")
					.summary("주문 다건 조회 실패 API - CUSTOMER 권한의 경우")
					.pathParameters(
						parameterWithName("userNumber").description("사용자 번호")
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
	@DisplayName("권한 - ADMIN 인 경우, 삭제되지 않은 주문을 조회하는 서비스를 호출 시 200 성공을 기대합니다.")
	void When_GetOrdersWithAdmin_Expect_Status200() throws Exception {
		// given
		long userNumber = 1L;
		int pageNum = 0;
		int pageSize = 10;
		String requestYear = "2024";

		OrderResponseDTO expectedOrderResponseDTO = OrderResponseDTO.create(expectedOrderWithADMINUser);

		Member member = Member.from(MemberResponseDTO.from(expectedOrderWithADMINUser.getMember()));

		OrderPageRequestDTO orderPageRequestDTO = OrderPageRequestDTO.create(PageRequestDTO.create(pageNum, pageSize),
			1L, requestYear);

		PageInfo<OrderResponseDTO> expectedOrderResponseDTOPageInfo = new PageInfo<>(List.of(expectedOrderResponseDTO));

		given(orderService.getOrdersWithPaginationAndNoCondition(member, orderPageRequestDTO)).willReturn(
			expectedOrderResponseDTOPageInfo);

		given(userDetailsService.loadUserByJwt(any())).willReturn(
			new UserDetailsServiceImpl.CustomUserDetails(expectedOrderResponseDTO.getMember()));

		// when - then
		ResultActions response = mockMvc.perform(
				get("/v1/api/orders/users/{userNumber}", userNumber)
					.param("pageNum", String.valueOf(pageNum))
					.param("pageSize", String.valueOf(pageSize))
					.param("requestYear", requestYear)
					.param("userNumber", String.valueOf(userNumber))
					.contentType(APPLICATION_JSON)
					.accept(APPLICATION_JSON))
			.andExpect(status().isOk());
		response
			.andDo(MockMvcRestDocumentationWrapper.document(
				"get-orders-admin",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				ResourceDocumentation.resource(ResourceSnippetParameters.builder()
					.tag("order")
					.summary("주문 다건 조회 API - ADMIN 권한의 경우")
					.pathParameters(
						parameterWithName("userNumber").description("사용자 번호")
					)
					.responseFields(
						fieldWithPath("total").description("전체 주문 수"),
						fieldWithPath("list").description("주문 리스트"),
						fieldWithPath("list[].id").description("주문 ID"),
						fieldWithPath("list[].ordererNumber").description("주문자 번호"),
						fieldWithPath("list[].orderStatus").description("주문 상태"),
						fieldWithPath("list[].paidDate").description("결제 일자"),
						fieldWithPath("list[].shippingInfoId").description("배송지 ID"),
						fieldWithPath("list[].createdDate").description("생성 일자"),
						fieldWithPath("list[].updatedDate").description("수정 일자"),
						fieldWithPath("list[].deletedDate").description("삭제 일자"),
						fieldWithPath("list[].member").description("주문자 정보"),
						fieldWithPath("list[].member.memberNo").description("주문자 번호"),
						fieldWithPath("list[].member.email").description("이메일"),
						fieldWithPath("list[].member.firstName").description("이름"),
						fieldWithPath("list[].member.lastName").description("성"),
						fieldWithPath("list[].member.phoneNumber").description("전화번호"),
						fieldWithPath("list[].member.addressMain").description("주소"),
						fieldWithPath("list[].member.addressSub").description("상세주소"),
						fieldWithPath("list[].member.zipCode").description("우편번호"),
						fieldWithPath("list[].member.gender").description("성별"),
						fieldWithPath("list[].member.role").description("역할"),
						fieldWithPath("list[].member.status").description("상태"),
						fieldWithPath("list[].member.birthday").description("생년월일"),
						fieldWithPath("list[].member.lastLoginDate").description("마지막 로그인 일자"),
						fieldWithPath("list[].member.createdDate").optional().description("생성 일자"),
						fieldWithPath("list[].member.updatedDate").optional().description("수정 일자"),
						fieldWithPath("list[].member.deletedDate").optional().description("삭제 일자"),
						fieldWithPath("list[].ordersProductRelations").description("주문-상품 관계 리스트"),
						fieldWithPath("list[].ordersProductRelations[].groupId").description("주문 ID"),
						fieldWithPath("list[].ordersProductRelations[].productId").description("상품 ID"),
						fieldWithPath("list[].ordersProductRelations[].productQuantity").description("상품 수량"),
						fieldWithPath("list[].ordersProductRelations[].createdDate").description("생성 일자"),
						fieldWithPath("list[].ordersProductRelations[].updatedDate").description("수정 일자"),
						fieldWithPath("list[].ordersProductRelations[].deletedDate").description("삭제 일자"),
						fieldWithPath("pageNum").description("현재 페이지 번호"),
						fieldWithPath("pageSize").description("페이지 크기"),
						fieldWithPath("size").description("현재 페이지 크기"),
						fieldWithPath("startRow").description("현재 페이지 시작 행"),
						fieldWithPath("endRow").description("현재 페이지 끝 행"),
						fieldWithPath("pages").description("전체 페이지 수"),
						fieldWithPath("prePage").description("이전 페이지"),
						fieldWithPath("nextPage").description("다음 페이지"),
						fieldWithPath("isFirstPage").description("첫 페이지 여부"),
						fieldWithPath("isLastPage").description("마지막 페이지 여부"),
						fieldWithPath("hasPreviousPage").description("이전 페이지 여부"),
						fieldWithPath("hasNextPage").description("다음 페이지 여부"),
						fieldWithPath("navigatePages").description("네비게이션 페이지 수"),
						fieldWithPath("navigatepageNums").description("네비게이션 페이지 번호"),
						fieldWithPath("navigateFirstPage").description("첫 네비게이션 페이지"),
						fieldWithPath("navigateLastPage").description("마지막 네비게이션 페이지")
					).build())));
	}

	@Test
	@MockMember(role = Member.Role.ROLE_ADMIN)
	@DisplayName("권한 - ADMIN 인 경우, 주문 다건 조회 실패 케이스의 경우 404 반환과 올바른 응답값이 오는지 확인합니다.")
	void When_GetOrdersWithAdmin_Pagination_SpecificYear_Expect_Status404() throws Exception {
		// given
		long userNumber = 1L;
		int pageNum = 100;
		int pageSize = 0;
		String requestYear = "2024";

		OrderPageRequestDTO orderPageRequestDTO = OrderPageRequestDTO.create(PageRequestDTO.create(pageNum, pageSize),
			1L, requestYear);

		Member member = Member.from(MemberResponseDTO.from(expectedOrderWithADMINUser.getMember()));

		given(orderService.getOrdersWithPaginationAndNoCondition(member, orderPageRequestDTO)).willThrow(
			new NotFoundException(ORDER_NOT_FOUND));

		given(userDetailsService.loadUserByJwt(any())).willReturn(
			new UserDetailsServiceImpl.CustomUserDetails(
				MemberResponseDTO.from(expectedOrderWithADMINUser.getMember())));

		// when - then
		ResultActions response = mockMvc.perform(
				get("/v1/api/orders/users/{userNumber}", userNumber)
					.param("pageNum", String.valueOf(pageNum))
					.param("pageSize", String.valueOf(pageSize))
					.param("requestYear", requestYear)
					.param("userNumber", String.valueOf(userNumber))
					.contentType(APPLICATION_JSON)
					.accept(APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value(ORDER_NOT_FOUND.getMessage()));

		response
			.andDo(MockMvcRestDocumentationWrapper.document(
				"get-orders-fail-admin",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				ResourceDocumentation.resource(ResourceSnippetParameters.builder()
					.tag("order")
					.summary("주문 다건 조회 실패 API - ADMIN 권한의 경우")
					.pathParameters(
						parameterWithName("userNumber").description("사용자 번호")
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