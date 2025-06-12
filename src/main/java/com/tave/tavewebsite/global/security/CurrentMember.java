package com.tave.tavewebsite.global.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? T(com.tave.tavewebsite.domain.member.exception.NotFoundMemberException).throwException() : member")
public @interface CurrentMember {

    /*
    * NOTE - 의논 사항
    * 현재 CustomUserDetails는 Memeber를 필드로 가짐
    * 즉, Token이 담긴 요청에 대해서는 항상 Member를 조회해옴.
    * 이는 비효율 적이므로 CustomUserDetails에 Member대신 Token의 MemberId를 사용하는 건 어떨 지?
    * */

}