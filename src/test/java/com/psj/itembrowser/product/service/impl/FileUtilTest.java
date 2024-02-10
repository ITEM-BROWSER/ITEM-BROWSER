package com.psj.itembrowser.product.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class FileUtilTest {

	@Test
	@DisplayName("유효한 파일 타입과 이름으로 검증 성공")
	void validateImageFileWithValidFile() {
		// given
		MultipartFile validFile = new MockMultipartFile("image", "test.jpg", "image/jpeg",
			"validateImageFile".getBytes());

		// when & then
		assertDoesNotThrow(() -> FileUtil.validateImageFile(validFile));
	}

	@Test
	@DisplayName("잘못된 MIME 타입으로 인한 검증 실패")
	void validateImageFileWithInvalidMimeType() {
		// given
		MultipartFile invalidTypeFile = new MockMultipartFile("image", "test.txt", "text/plain",
			"InvalidMimeType".getBytes());

		// when & then
		assertThrows(IllegalArgumentException.class,
			() -> FileUtil.validateImageFile(invalidTypeFile),
			"Invalid file type, only image files are allowed.");
	}

	@Test
	@DisplayName("유효하지 않은 파일 이름으로 인한 검증 실패")
	void validateFileNameWithInvalidName() {
		// given
		MultipartFile invalidTypeFile = new MockMultipartFile("image", "test/../test.jpg",
			"text/plain", "InvalidName".getBytes());

		// when & then
		assertThrows(IllegalArgumentException.class,
			() -> FileUtil.validateImageFile(invalidTypeFile),
			"Filename is invalid.");
	}

	@Test
	@DisplayName("파일 이름이 null 검증 실패")
	void validateFileNameWithNull() {
		// given
		MultipartFile invalidTypeFile = new MockMultipartFile("image", null,
			"text/plain", "NameIsNull".getBytes());

		// when & then
		assertThrows(IllegalArgumentException.class,
			() -> FileUtil.validateImageFile(invalidTypeFile),
			"Filename is invalid.");
	}

	@Test
	@DisplayName("특수 문자가 포함된 파일 이름으로 인한 검증 실패")
	void validateFileNameWithSpecialCharacters() {
		// given
		MultipartFile invalidTypeFile = new MockMultipartFile("image", "test*?.jpg", "text/plain",
			"SpecialCharacters".getBytes());

		// when & then
		assertThrows(IllegalArgumentException.class,
			() -> FileUtil.validateImageFile(invalidTypeFile),
			"Filename contains invalid characters.");
	}

	@Test
	@DisplayName("이미지 파일이 3개 미만 검증 실패")
	public void validateNumberOfImageFiles() {
		// given
		List<MultipartFile> files = List.of(
			new MockMultipartFile("file", "filename", "image/jpeg", new byte[] {1}));

		// when & then
		assertThatThrownBy(() -> FileUtil.validateNumberOfImageFiles(files))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Images must be at least 3 and not more than 10");
	}
}