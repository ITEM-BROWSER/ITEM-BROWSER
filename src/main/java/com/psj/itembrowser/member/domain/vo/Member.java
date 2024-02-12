package com.psj.itembrowser.member.domain.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.psj.itembrowser.member.domain.dto.request.MemberRequestDTO;
import com.psj.itembrowser.member.domain.dto.response.MemberResponseDTO;
import com.psj.itembrowser.security.common.BaseDateTimeEntity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(of = {"no", "credentials"}, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseDateTimeEntity {

	private MemberNo no;

	/**
	 * 인증정보
	 */
	private Credentials credentials;

	/**
	 * 성. 이름
	 */
	private Name name;

	/**
	 * 휴대폰번호
	 */
	private String phoneNumber;

	/**
	 * 성별
	 */
	private Gender gender;

	/**
	 * 역할
	 */
	private Role role;

	/**
	 * 회원 상태. ACTIVE -> 활성화, READY -> 대기, DISABLED -> 비활성화
	 */
	private Status status = Status.ACTIVE;

	private MemberShipType memberShipType = MemberShipType.REGULAR;

	/**
	 * 주소
	 */
	private Address address;

	/**
	 * 생년월일. 생년월일
	 */
	private LocalDate birthday;

	/**
	 * 최종 로그인 일시
	 */
	private LocalDateTime lastLoginDate;

	public static Member from(MemberRequestDTO dto) {
		Member member = new Member();
		member.credentials = new Credentials(dto.getMemberId(), dto.getPassword());
		return member;
	}

	public static Member from(MemberResponseDTO dto) {
		Member member = new Member();

		member.no = new MemberNo(dto.getMemberNo());
		member.credentials = new Credentials(dto.getEmail(), dto.getPassword());
		member.name = new Name(dto.getFirstName(), dto.getLastName());
		member.phoneNumber = dto.getPhoneNumber();
		member.gender = dto.getGender(); // Gender enum에 맞게 변환
		member.role = dto.getRole(); // Role enum에 맞게 변환
		member.status = dto.getStatus(); // Status enum에 맞게 변환
		member.address = new Address(dto.getAddressMain(), dto.getAddressSub(), dto.getZipCode());
		member.birthday = dto.getBirthday();
		member.lastLoginDate = dto.getLastLoginDate();

		return member;
	}

	public boolean isSame(Member other) {
		if (other == null) {
			return false;
		}

		return Objects.equals(this, other);
	}

	public boolean hasRole(Role role) {
		return this.role == role;
	}

	@Getter
	public enum Role {
		ROLE_CUSTOMER("일반 구매자"),
		ROLE_STORE_SELLER("상점 판매자"),
		ROLE_ADMIN("관리자");

		private final String name;
		private final String description;

		Role(String description) {
			this.description = description;
			this.name = this.name();
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	@Getter
	public enum Status {
		ACTIVE("활성화"),
		READY("대기"),
		DISABLED("비활성화");

		private final String name;
		private final String description;

		Status(String description) {
			this.description = description;
			this.name = this.name();
		}
	}

	@Getter
	public enum Gender {
		MEN("남성"),
		WOMEN("여성");

		private final String name;
		private final String description;

		Gender(String description) {
			this.description = description;
			this.name = this.name();
		}
	}

	@Getter
	public enum MemberShipType {
		REGULAR("일반 회원", 0),
		WOW("와우 회원", 10);

		private final String name;
		private final String description;
		private final int discountRate;

		MemberShipType(String description, int discountRate) {
			this.name = this.name();
			this.description = description;
			this.discountRate = discountRate;
		}

		@Override
		public String toString() {
			return this.name();
		}
	}
}