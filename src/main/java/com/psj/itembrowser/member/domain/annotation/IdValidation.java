package com.psj.itembrowser.member.domain.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented // 해당 어노테이션도 javadoc에 포함된다.
@Target({ElementType.FIELD}) // 필드위치에만 적용이 가능하다.
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME) // HTTP 통신을 통하여 런타임 단계에서도 Password 의 유효성 검사가 필요하다.
public @interface IdValidation {
    // 유효성 검사 실패시 반환되는 기본 오류 메서드에 대해서 정의한다.
    String message() default "아이디: 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.";
    
    // 유효성 검사를 그룹화한다. 특정 상황이나 컨텍스트에서 일부 검증을 수행할 수 있음. ex ) 사용자 등록시 검사, 사용자 정보 업데이트시 검사 X 등...
    Class[] groups() default {};
    
    // payload -> 유효성 검사 실패시 심각도.. 오류 코드등을 전달시에 사용한다. 클라이언트 측에서 오류메시지를 다룰 떄 사용
    Class[] payload() default {};
}