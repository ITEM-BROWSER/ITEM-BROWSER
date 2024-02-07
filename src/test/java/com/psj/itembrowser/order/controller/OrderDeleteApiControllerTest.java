package com.psj.itembrowser.order.controller;

import static java.text.MessageFormat.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.psj.itembrowser.order.service.OrderService;
import com.psj.itembrowser.security.common.exception.DatabaseOperationException;
import com.psj.itembrowser.security.common.exception.ErrorCode;
import com.psj.itembrowser.security.service.impl.UserDetailsServiceImpl;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(OrderApiController.class)
@AutoConfigureRestDocs
public class OrderDeleteApiControllerTest {
	private final String BASE_URL = "/v1/api/orders";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	@MockBean
	private UserDetailsServiceImpl userDetailsService;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext,
		RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
			.build();
	}

	@Test
	@DisplayName("주문 삭제 성공에 대해 200 성공 메시지가 반환되는지 확인합니다.")
	void When_DeleteOrder_Expect_Status200() throws Exception {
		// given
		long orderId = 1L;
		String message = format("Order record for {0} has been deleted.", orderId);
		// when

		// then
		mockMvc.perform(RestDocumentationRequestBuilders.delete(BASE_URL + "/{orderId}", orderId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value(message))
			.andDo(document("delete-order"
				, preprocessRequest(prettyPrint())
				, preprocessResponse(prettyPrint())
				, ResourceDocumentation.resource(
					ResourceSnippetParameters.builder()
						.tag("order")
						.summary("주문 삭제")
						.description("주문 삭제 API 입니다.")
						.pathParameters(
							parameterWithName("orderId").description("주문 ID")
						)
						.responseFields(
							fieldWithPath("message").description("결과 메시지")
						)
						.build()
				)
			));
	}

	@Test
	@DisplayName("주문 삭제 실패가 발생시 주문 삭제에 예외가 발생되는지 체크해야합니다.")
	void When_DeleteOrder_Expect_Status400() throws Exception {
		// given
		long orderId = 1L;
		doThrow(new DatabaseOperationException(ErrorCode.ORDER_DELETE_FAIL))
			.when(orderService)
			.removeOrder(orderId);

		// when

		// then
		mockMvc.perform(RestDocumentationRequestBuilders.delete(BASE_URL + "/{orderId}", orderId))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(ErrorCode.ORDER_DELETE_FAIL.getMessage()))
			.andDo(document("delete-order-fail"
				, preprocessRequest(prettyPrint())
				, preprocessResponse(prettyPrint())
				, ResourceDocumentation.resource(
					ResourceSnippetParameters.builder()
						.tag("order")
						.summary("주문 삭제 실패")
						.description("주문 삭제 실패 API 입니다.")
						.pathParameters(
							parameterWithName("orderId").description("주문 ID")
						)
						.responseFields(
							fieldWithPath("status").description("오류 상태 코드"),
							fieldWithPath("error").description("에러 메시지"),
							fieldWithPath("message").description("오류 메시지"),
							fieldWithPath("path").description("URI"),
							fieldWithPath("timestamp").description("시간")
						)
						.build()
				)
			));
	}
}