package com.psj.itembrowser.member.domain.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import com.psj.itembrowser.member.domain.annotation.PasswordValidation;
import com.psj.itembrowser.member.domain.vo.Member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link Member}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpRequestDTO {

	private Long memberNo;

	@NotNull
	@Email(message = "이메일 형식이 아닙니다.")
	private String credentialsEmail;

	//영문/숫자/특수문자 2가지 이상 조합 (8~20자)
	//3개 이상 연속되거나 동일한 문자/숫자 제외
	@NotNull
	@PasswordValidation
	private String credentialsPassword;

	@NotNull
	private String nameFirstName;

	@NotNull
	private String nameLastName;

	@NotNull
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$")
	private String contactPhoneNumber;
	@NotNull
	private Member.Gender gender;
	@NotNull
	private Member.Role role;
	private Member.Status status;
	private Member.MemberShipType memberShipType;
	private String addressAddressMain;
	private String addressAddressSub;
	private String addressZipCode;
	@PastOrPresent
	private LocalDate birthday;
}