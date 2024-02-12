package com.psj.itembrowser.member.domain.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.psj.itembrowser.member.domain.vo.Member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link Member}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDTO {
	private Long memberNo;
	private String email;
	@JsonIgnore
	private String password;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String addressMain;
	private String addressSub;
	private String zipCode;
	private Member.Gender gender;
	private Member.Role role;
	private Member.Status status;
	private Member.MemberShipType memberShipType;
	private LocalDate birthday;
	private LocalDateTime lastLoginDate;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private LocalDateTime deletedDate;

	public static MemberResponseDTO from(Member member) {
		MemberResponseDTO memberResponseDTO = new MemberResponseDTO();

		memberResponseDTO.setMemberNo(member.getNo().getMemberNo());
		memberResponseDTO.setPassword(member.getCredentials().getPassword());
		memberResponseDTO.setEmail(member.getCredentials().getEmail());
		memberResponseDTO.setFirstName(member.getName().getFirstName());
		memberResponseDTO.setLastName(member.getName().getLastName());
		memberResponseDTO.setPhoneNumber(member.getPhoneNumber());
		memberResponseDTO.setAddressMain(member.getAddress().getAddressMain());
		memberResponseDTO.setAddressSub(member.getAddress().getAddressSub());
		memberResponseDTO.setZipCode(member.getAddress().getZipCode());
		memberResponseDTO.setGender(member.getGender());
		memberResponseDTO.setRole(member.getRole());
		memberResponseDTO.setStatus(member.getStatus());
		memberResponseDTO.setMemberShipType(member.getMemberShipType());
		memberResponseDTO.setBirthday(member.getBirthday());
		memberResponseDTO.setLastLoginDate(member.getLastLoginDate());
		memberResponseDTO.setCreatedDate(member.getCreatedDate());
		memberResponseDTO.setUpdatedDate(member.getUpdatedDate());
		memberResponseDTO.setDeletedDate(member.getDeletedDate());

		return memberResponseDTO;
	}
}