package com.psj.itembrowser.member.mapper;

import com.psj.itembrowser.member.domain.vo.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberMapperTest {
    
    @Autowired
    private MemberMapper memberMapper;
    
    @Test
    @Sql(statements = {"INSERT INTO MEMBER (EMAIL, PASSWORD, FIRST_NAME , LAST_NAME , GENDER , PHONE_NUMBER , BIRTHDAY , ROLE, STATUS) " +
            "VALUES ('validUserId@naver.com', 'validPassword' , 'sangjun','park','MEN','010-1234-5678','1994-01-06','ROLE_CUSTOMER','ACTIVE')"})
    @DisplayName("존재하는 유저 아이디로 조회 시 Member 가 반환되는지 확인합니다.")
    public void When_FindByUserId_Expect_Member() {
        // given
        String validEmail = "validUserId@naver.com";
        
        // when
        Member member = memberMapper.findByEmail(validEmail);
        
        // then
        assertThat(member).isNotNull();
        assertThat(member.getCredentials().getEmail()).isEqualTo(validEmail);
    }
    
    @Test
    @DisplayName("존재하지 않는 유저 이메일 조회 시 null 이 반환되는지 확인합니다.")
    public void When_FindByInvalidEmail_Expect_Null() {
        // given
        String invalidEmail = "^^@naver.com";
        
        // when
        Member member = memberMapper.findByEmail(invalidEmail);
        
        // then
        assertThat(member).isNull();
    }
}