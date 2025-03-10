package com.tave.tavewebsite.global.security.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    private static final int MAX_AGE_OF_COOKIE = 3 * 24 * 60 * 60;

    public static void setCookie(HttpServletResponse response, String name, String value) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .maxAge(MAX_AGE_OF_COOKIE)
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "value")
                .maxAge(0)
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }
}
