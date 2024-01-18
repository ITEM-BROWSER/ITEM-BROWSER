package com.psj.itembrowser.member.domain.dto.response;

import com.psj.itembrowser.member.domain.vo.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link Member}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDTO {
    private Long memberNo;
    private String email;
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
    private LocalDate birthday;
    private LocalDateTime lastLoginDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime deletedDate;
}