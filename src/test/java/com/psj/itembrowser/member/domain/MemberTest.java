package com.psj.itembrowser.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.member.domain.vo.Member.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;


@DisplayName("Member 도메인 테스트")
public class MemberTest {
    
    @Test
    @DisplayName("회원이 ACTIVE 인 경우 isActivated() 메서드는 true 를 반환한다.")
    public void When_StatusIsActive_Expect_True() {
        // given
        Member member = new Member();
        ReflectionTestUtils.setField(member, "status", Status.ACTIVE);
        
        // when
        boolean result = member.isActivated();
        
        // then
        assertThat(result).isTrue();
    }
    
    @Test
    @DisplayName("회원이 ACTIVE 가 아닌 경우 isActivated() 메서드는 false 를 반환한다.")
    public void When_StatusIsDisabled_Expect_False() {
        // given
        Member member = new Member();
        ReflectionTestUtils.setField(member, "status", Status.DISABLED);
        
        // when
        boolean result = member.isActivated();
        
        // then
        assertThat(result).isFalse();
    }
}