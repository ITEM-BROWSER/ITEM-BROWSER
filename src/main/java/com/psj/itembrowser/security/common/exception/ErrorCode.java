package com.psj.itembrowser.security.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.psj.itembrowser.common.exception fileName       : ErrorCode author         :
 * ipeac date           : 2023-11-05 description    :
 * =========================================================== DATE              AUTHOR NOTE
 * ----------------------------------------------------------- 2023-11-05        ipeac       최초 생성
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
	// Cart
	CART_NOT_FOUND(HttpStatus.NOT_FOUND, "CART_001", "Not Found Cart"),
	CART_INSERT_FAIL(HttpStatus.BAD_REQUEST, "CART_002", "Fail to add to Cart"),
	CART_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "CART_003", "Fail to find Cart Product"),
	CART_PRODUCT_INSERT_FAIL(HttpStatus.BAD_REQUEST, "CART_004", "Fail to insert Cart Product"),
	CART_PRODUCT_UPDATE_FAIL(HttpStatus.BAD_REQUEST, "CART_005", "Fail to update Cart Product"),
	CART_PRODUCT_DELETE_FAIL(HttpStatus.BAD_REQUEST, "CART_006", "Fail to delete Cart Product"),
	CART_PRODUCT_QUANTITY_NOT_POSITIVE(HttpStatus.BAD_REQUEST, "CART_007",
		"Quantity must be positive"),

	// Order
	ORDER_DELETE_FAIL(HttpStatus.BAD_REQUEST, "ORDER_001", "Fail to delete Order"),
	ORDER_RELATION_DELETE_FAIL(HttpStatus.BAD_REQUEST, "ORDER_002",
		"Fail to delete Order Relation"),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_003", "Not Found Order"),
	ORDER_NOT_CANCELABLE(HttpStatus.BAD_REQUEST, "ORDER_004", "Order is not cancelable"),
	ORDER_IS_NOT_MATCH_CURRENT_MEMBER(HttpStatus.BAD_REQUEST, "ORDER_005",
		"Order is not match current member"),

	// common
	COMMON_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_001", "Internal Server Error"),
	INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "COMMON_002", "Invalid Date Format"),

	// Member
	NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "MEMBER_001", "Not Found Member"),
	MEMBER_INSERT_FAIL(HttpStatus.BAD_REQUEST, "MEMBER_002", "Fail to insert Member"),

	// Product
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_001", "Not Found Product"),
	PRODUCT_INSERT_FAIL(HttpStatus.BAD_REQUEST, "PRODUCT_002", "Fail to insert to product"),
	PRODUCT_UPDATE_FAIL(HttpStatus.BAD_REQUEST, "PRODUCT_003", "Fail to update to product"),
	PRODUCT_DELETE_FAIL(HttpStatus.BAD_REQUEST, "PRODUCT_004", "Fail to delete to product"),
	PRODUCT_VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "PRODUCT_005", "Validation Error"),

	//Token
	ACCESS_TOKEN_NOT_GENERATED(HttpStatus.UNAUTHORIZED, "TOKEN_001",
		"Fail to generate Access Token"),
	REFRESH_TOKEN_NOT_GENERATED(HttpStatus.UNAUTHORIZED, "TOKEN_002",
		"Fail to generate Refresh Token"),
	TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "TOKEN_003", "Not Found Token"),
	NOT_FOUND_SUBJECT(HttpStatus.NOT_FOUND, "TOKEN_004,", "Not Found Subject"),

	//Auth
	SELLER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_001", "Seller is not authorized"),
	ADMIN_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_002", "Admin is not authorized"),
	CUSTOMER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_003", "Customer is not authorized"),
	PRINCIPAL_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH_004", "Principal is not found"),
	INVALID_MEMBER_ROLE(HttpStatus.UNAUTHORIZED, "AUTH_005", "Invalid Member Role"),
	;

	private final HttpStatus status;
	private final String code;
	private final String message;
}