package com.psj.itembrowser.security.common.openfeign.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDto {

	private Common common;
	private List<Juso> juso;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Common {
		private String totalCount;
		private Integer currentPage;
		private Integer countPerPage;
		private String errorCode;
		private String errorMessage;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Juso {
		private String roadAddr;
		private String roadAddrPart1;
		private String roadAddrPart2;
		private String jibunAddr;
		private String engAddr;
		private String zipNo;
		private String admCd;
		private String rnMgtSn;
		private String bdMgtSn;
		private String detBdNmList;
		private String bdNm;
		private String bdKdcd;
		private String siNm;
		private String sggNm;
		private String emdNm;
		private String liNm;
		private String rn;
		private String udrtYn;
		private Integer buldMnnm;
		private Integer buldSlno;
		private String mtYn;
		private Integer lnbrMnnm;
		private Integer lnbrSlno;
		private String emdNo;
		private String hstryYn;
		private String relJibun;
		private String hemdNm;
	}
}