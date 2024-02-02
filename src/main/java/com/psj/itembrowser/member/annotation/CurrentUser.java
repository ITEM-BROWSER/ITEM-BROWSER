package com.psj.itembrowser.member.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

/**
 * {@link org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver} <- reference
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {

}