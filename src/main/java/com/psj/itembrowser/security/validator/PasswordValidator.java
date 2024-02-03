package com.psj.itembrowser.security.validator;

import com.psj.itembrowser.member.domain.annotation.PasswordValidation;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NonNull;

public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {
    
    // 비밀번호는 8자 이상, 20자 이하, 영문 대소문자, 숫자, 특수문자가 각각 1개 이상 포함되어야 함
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,20}$";
    private static final Pattern match = Pattern.compile(PASSWORD_PATTERN);
    
    /**
     * 메서드 유효성 검사를 수행하기 전에 호출됨.
     * 제약 조건 어노테이션의 인스턴스 받아 초기화를 수행함.
     *
     * @param passwordAnnotation 어노테이션의 메타데이터
     */
    @Override
    public void initialize(PasswordValidation passwordAnnotation) {
        // 필요하면 추가
    }
    
    /**
     * 실제 유효성 검사 로직을 수행
     *
     * @param password                   검사의 대상
     * @param constraintValidatorContext 유효성검사에 대한 메타데이터
     * @return 유값이 유효한 경우 true, 그렇지 않은 경우 false를 반환
     */
    @Override
    public boolean isValid(@NonNull String password, ConstraintValidatorContext constraintValidatorContext) {
        if (match.matcher(password).matches() == false || isNotPasswordContinuousThreeTimes(password) || isNotPasswordRepeatedThreeTimes(password)) {
            return false;
        }
        
        return true;
    }
    
    private boolean isNotPasswordRepeatedThreeTimes(String password) {
        // word character (\\w) -> [a-zA-Z0-9]
        if (password.matches("(\\w)\\1\\1")) {
            return false;
        }
        return true;
    }
    
    private boolean isNotPasswordContinuousThreeTimes(String password) {
        // 연속된 1, 2, 3  이나 a, b, c  가 연속된 경우 true
        for (int i = 0; i < password.length(); i++) {
            if (i + 2 < password.length()) {
                if (password.charAt(i) + 1 == password.charAt(i + 1) && password.charAt(i) + 2 == password.charAt(i + 2)) {
                    return false;
                }
            }
        }
        return true;
    }
}