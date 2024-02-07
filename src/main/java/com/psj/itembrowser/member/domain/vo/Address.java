package com.psj.itembrowser.member.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * packageName    : com.psj.itembrowser.member.domain.vo
 * fileName       : MemberName
 * author         : ipeac
 * date           : 2024-01-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-06        ipeac       최초 생성
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	/**
	 * 주소 메인
	 */
	private String addressMain;
	/**
	 * 주소 서브
	 */
	private String addressSub;
	/**
	 * 우편번호
	 */
	private String zipCode;

	public static Address create(String addressMain, String addressSub, String zipCode) {
		return new Address(addressMain, addressSub, zipCode);
	}
}