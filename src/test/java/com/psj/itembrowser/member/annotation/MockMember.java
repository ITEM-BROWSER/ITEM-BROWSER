package com.psj.itembrowser.member.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.member.factory.MockMemberSecurityContextFactory;

/**
 * packageName    : com.psj.itembrowser.member.annotation
 * fileName       : MockMember
 * author         : ipeac
 * date           : 2024-02-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-01        ipeac       최초 생성
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockMemberSecurityContextFactory.class)
public @interface MockMember {
	long memberNo() default 1L;
	
	String email() default "mockMember3410@gmail.com";
	
	String password() default "3410";
	
	String firstName() default "gildong";
	
	String lastName() default "hong";
	
	String phoneNumber() default "010-1234-5678";
	
	Member.Gender gender() default Member.Gender.MEN;
	
	Member.Role role() default Member.Role.ROLE_CUSTOMER;
	
	Member.Status status() default Member.Status.ACTIVE;
	
	String addressMain() default "서울시 강남구";
	
	String addressSub() default "김밥빌딩 101동 302호";
	
	String zipCode() default "01012";
}