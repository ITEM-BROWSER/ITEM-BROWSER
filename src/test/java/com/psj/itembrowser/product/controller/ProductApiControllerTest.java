package com.psj.itembrowser.product.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.psj.itembrowser.product.domain.dto.request.ProductRequestDTO;
import com.psj.itembrowser.product.domain.dto.request.ProductUpdateDTO;
import com.psj.itembrowser.product.service.ProductService;
import com.psj.itembrowser.security.common.exception.BadRequestException;
import com.psj.itembrowser.security.common.exception.ErrorCode;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(ProductApiController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ProductApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext,
		RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
			.build();
	}

	@Test
	@DisplayName("상품 생성 API 테스트")
	void createProductTest() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg",
			"product create test".getBytes());

		doNothing().when(productService).createProduct(any(ProductRequestDTO.class));

		mockMvc.perform(RestDocumentationRequestBuilders.multipart("/v1/api/products")
				.file(file)
				.param("name", "Test Product")
				.param("category", "1")
				.param("detail", "This is a detailed description of the product.")
				.param("status", "COMPLETED")
				.param("quantity", "100")
				.param("unitPrice", "20000")
				.param("sellerId", "test@test.com")
				.param("sellStartDatetime", "2026-02-05T13:19:37")
				.param("sellEndDatetime", "2026-02-07T13:19:37")
				.param("displayName", "Test Display Name")
				.param("brand", "Test Brand")
				.param("deliveryFeeType", "FREE")
				.param("deliveryMethod", "Direct Delivery")
				.param("deliveryDefaultFee", "0")
				.param("freeShipOverAmount", "50000")
				.param("returnCenterCode", "RC12345")
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(MockMvcRestDocumentationWrapper.document(
					"create-product",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					ResourceDocumentation.resource(
						ResourceSnippetParameters.builder()
							.tag("product")
							.summary("상품 정보 추가 API")
							.requestParameters(
								parameterWithName("name").description("상품 이름"),
								parameterWithName("category").description("카테고리 ID"),
								parameterWithName("detail").description("상품 상세 설명"),
								parameterWithName("status").description("상품 상태"),
								parameterWithName("quantity").description("상품 수량"),
								parameterWithName("unitPrice").description("단가"),
								parameterWithName("sellerId").description("판매자 ID"),
								parameterWithName("sellStartDatetime").description("판매 시작 일시"),
								parameterWithName("sellEndDatetime").description("판매 종료 일시"),
								parameterWithName("displayName").description("상품 표시 이름"),
								parameterWithName("brand").description("브랜드"),
								parameterWithName("deliveryFeeType").description("배송료 유형"),
								parameterWithName("deliveryMethod").description("배송 방법"),
								parameterWithName("deliveryDefaultFee").description("기본 배송료"),
								parameterWithName("freeShipOverAmount").description("무료 배송 기준 금액"),
								parameterWithName("returnCenterCode").description("반품 센터 코드")
							)
							.responseFields(
								fieldWithPath("message").description("응답 메시지")
							)
							.build()
					)
				)
			);

		verify(productService, times(1)).createProduct(any(ProductRequestDTO.class));
	}

	@Test
	@DisplayName("상품 생성 API 실패 테스트")
	void createProductTestFail() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg",
			"product create test".getBytes());

		doThrow(new BadRequestException(ErrorCode.PRODUCT_INSERT_FAIL)).when(productService)
			.createProduct(any(ProductRequestDTO.class));

		mockMvc.perform(RestDocumentationRequestBuilders.multipart("/v1/api/products")
				.file(file)
				.param("name", "Test Product")
				.param("category", "1")
				.param("detail", "This is a detailed description of the product.")
				.param("status", "COMPLETED")
				.param("quantity", "100")
				.param("unitPrice", "20000")
				.param("sellerId", "test@test.com")
				.param("sellStartDatetime", "2026-02-05T13:19:37")
				.param("sellEndDatetime", "2026-02-07T13:19:37")
				.param("displayName", "Test Display Name")
				.param("brand", "Test Brand")
				.param("deliveryFeeType", "FREE")
				.param("deliveryMethod", "Direct Delivery")
				.param("deliveryDefaultFee", "0")
				.param("freeShipOverAmount", "50000")
				.param("returnCenterCode", "RC12345")
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(MockMvcRestDocumentationWrapper.document(
					"create-product-fail",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					ResourceDocumentation.resource(
						ResourceSnippetParameters.builder()
							.tag("product")
							.summary("상품 정보 추가 API")
							.requestParameters(
								parameterWithName("name").description("상품 이름"),
								parameterWithName("category").description("카테고리 ID"),
								parameterWithName("detail").description("상품 상세 설명"),
								parameterWithName("status").description("상품 상태"),
								parameterWithName("quantity").description("상품 수량"),
								parameterWithName("unitPrice").description("단가"),
								parameterWithName("sellerId").description("판매자 ID"),
								parameterWithName("sellStartDatetime").description("판매 시작 일시"),
								parameterWithName("sellEndDatetime").description("판매 종료 일시"),
								parameterWithName("displayName").description("상품 표시 이름"),
								parameterWithName("brand").description("브랜드"),
								parameterWithName("deliveryFeeType").description("배송료 유형"),
								parameterWithName("deliveryMethod").description("배송 방법"),
								parameterWithName("deliveryDefaultFee").description("기본 배송료"),
								parameterWithName("freeShipOverAmount").description("무료 배송 기준 금액"),
								parameterWithName("returnCenterCode").description("반품 센터 코드")
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
				)
			);

		verify(productService, times(1)).createProduct(any(ProductRequestDTO.class));
	}

	@Test
	@DisplayName("상품 수정 API 성공 테스트")
	void modifyProductTest() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg",
			"product create test".getBytes());
		Long productId = 1L;

		doNothing().when(productService)
			.updateProduct(any(ProductUpdateDTO.class), any(Long.class));

		mockMvc.perform(
				RestDocumentationRequestBuilders.multipart("/v1/api/products/{productId}", productId)
					.file(file)
					.with(request -> {
						request.setMethod("PUT");
						return request;
					})
					.param("name", "Test Product")
					.param("category", "1")
					.param("detail", "This is a detailed description of the product.")
					.param("status", "COMPLETED")
					.param("quantity", "100")
					.param("unitPrice", "20000")
					.param("sellerId", "test@test.com")
					.param("sellStartDatetime", "2024-02-06T13:19:37")
					.param("sellEndDatetime", "2024-02-06T13:19:37")
					.param("displayName", "Test Display Name")
					.param("brand", "Test Brand")
					.param("deliveryFeeType", "FREE")
					.param("deliveryMethod", "Direct Delivery")
					.param("deliveryDefaultFee", "0")
					.param("freeShipOverAmount", "50000")
					.param("returnCenterCode", "RC12345")
					.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(MockMvcRestDocumentationWrapper.document(
					"modify-product",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					ResourceDocumentation.resource(
						ResourceSnippetParameters.builder()
							.tag("product")
							.summary("상품 정보 수정 API")
							.requestParameters(
								parameterWithName("name").description("상품 이름"),
								parameterWithName("category").description("카테고리 ID"),
								parameterWithName("detail").description("상품 상세 설명"),
								parameterWithName("status").description("상품 상태"),
								parameterWithName("quantity").description("상품 수량"),
								parameterWithName("unitPrice").description("단가"),
								parameterWithName("sellerId").description("판매자 ID"),
								parameterWithName("sellStartDatetime").description("판매 시작 일시"),
								parameterWithName("sellEndDatetime").description("판매 종료 일시"),
								parameterWithName("displayName").description("상품 표시 이름"),
								parameterWithName("brand").description("브랜드"),
								parameterWithName("deliveryFeeType").description("배송료 유형"),
								parameterWithName("deliveryMethod").description("배송 방법"),
								parameterWithName("deliveryDefaultFee").description("기본 배송료"),
								parameterWithName("freeShipOverAmount").description("무료 배송 기준 금액"),
								parameterWithName("returnCenterCode").description("반품 센터 코드")
							)
							.responseFields(
								fieldWithPath("message").description("응답 메시지")
							)
							.build()
					)
				)
			);

		verify(productService, times(1)).updateProduct(any(ProductUpdateDTO.class),
			any(Long.class));
	}

	@Test
	@DisplayName("상품 수정 API 실패 테스트")
	void modifyProductTestFail() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg",
			"product create test".getBytes());
		Long productId = 1L;

		doThrow(new BadRequestException(ErrorCode.PRODUCT_UPDATE_FAIL)).when(productService)
			.updateProduct(any(ProductUpdateDTO.class), any(Long.class));

		mockMvc.perform(
				RestDocumentationRequestBuilders.multipart("/v1/api/products/{productId}", productId)
					.file(file)
					.with(request -> {
						request.setMethod("PUT");
						return request;
					})
					.param("name", "Test Product")
					.param("category", "1")
					.param("detail", "This is a detailed description of the product.")
					.param("status", "COMPLETED")
					.param("quantity", "100")
					.param("unitPrice", "20000")
					.param("sellerId", "test@test.com")
					.param("sellStartDatetime", "2026-02-05T13:19:37")
					.param("sellEndDatetime", "2026-02-07T13:19:37")
					.param("displayName", "Test Display Name")
					.param("brand", "Test Brand")
					.param("deliveryFeeType", "FREE")
					.param("deliveryMethod", "Direct Delivery")
					.param("deliveryDefaultFee", "0")
					.param("freeShipOverAmount", "50000")
					.param("returnCenterCode", "RC12345")
					.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(MockMvcRestDocumentationWrapper.document(
					"modify-product-fail",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					ResourceDocumentation.resource(
						ResourceSnippetParameters.builder()
							.tag("product")
							.summary("상품 정보 수정 API")
							.requestParameters(
								parameterWithName("name").description("상품 이름"),
								parameterWithName("category").description("카테고리 ID"),
								parameterWithName("detail").description("상품 상세 설명"),
								parameterWithName("status").description("상품 상태"),
								parameterWithName("quantity").description("상품 수량"),
								parameterWithName("unitPrice").description("단가"),
								parameterWithName("sellerId").description("판매자 ID"),
								parameterWithName("sellStartDatetime").description("판매 시작 일시"),
								parameterWithName("sellEndDatetime").description("판매 종료 일시"),
								parameterWithName("displayName").description("상품 표시 이름"),
								parameterWithName("brand").description("브랜드"),
								parameterWithName("deliveryFeeType").description("배송료 유형"),
								parameterWithName("deliveryMethod").description("배송 방법"),
								parameterWithName("deliveryDefaultFee").description("기본 배송료"),
								parameterWithName("freeShipOverAmount").description("무료 배송 기준 금액"),
								parameterWithName("returnCenterCode").description("반품 센터 코드")
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
				)
			);

		verify(productService, times(1)).updateProduct(any(ProductUpdateDTO.class),
			any(Long.class));
	}
}