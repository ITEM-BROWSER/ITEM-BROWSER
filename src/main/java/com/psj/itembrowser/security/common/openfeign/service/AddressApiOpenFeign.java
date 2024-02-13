package com.psj.itembrowser.security.common.openfeign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.psj.itembrowser.security.common.openfeign.dto.AddressResponseDto;

/**
 *packageName    : com.psj.itembrowser.security.openfeign
 * fileName       : AddressApiOpenFeign
 * author         : ipeac
 * date           : 2024-02-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-13        ipeac       최초 생성
 */
@FeignClient(name = "AddressApiOpenFeign", url = "${juso.url}")
public interface AddressApiOpenFeign {

	@GetMapping
	AddressResponseDto getAddress(
		@RequestParam("confmKey") String confmKey,
		@RequestParam(value = "currentPage", defaultValue = "1") String currentPage,
		@RequestParam(value = "countPerPage", defaultValue = "10") String countPerPage,
		@RequestParam("keyword") String keyword,
		@RequestParam(value = "resultType", defaultValue = "json") String resultType
	);
}