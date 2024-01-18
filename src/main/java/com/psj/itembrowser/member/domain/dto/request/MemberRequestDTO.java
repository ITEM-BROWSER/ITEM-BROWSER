package com.psj.itembrowser.member.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * DTO for {@link com.psj.itembrowser.member.domain.vo.Member}
 */
@Data
@AllArgsConstructor
public class MemberRequestDTO {
    @NotNull(message = "memberId must be not null")
    private final String memberId;
    @NotNull(message = "password must be not null")
    private final String password;
    @NotNull
    private final String firstName;
    @NotNull
    private final String lastName;
    @NotNull
    private final String email;
    @NotNull
    private final String phone;
}