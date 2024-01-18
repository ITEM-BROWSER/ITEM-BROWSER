package com.psj.itembrowser.member.domain.vo;

import lombok.*;

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
@EqualsAndHashCode(of = {"email", "password"})
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
    /**
     * 아이디
     */
    private String email;
    /**
     * 비밀번호
     */
    private String password;
}