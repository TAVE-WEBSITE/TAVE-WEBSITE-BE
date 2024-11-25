package com.tave.tavewebsite.global.security.exception;

import static com.tave.tavewebsite.global.security.exception.JwtErrorMessage.CANNOT_USE_REFRESHTOKEN;
import static com.tave.tavewebsite.global.security.exception.JwtErrorMessage.EXPIRED_JWT_TOKEN;
import static com.tave.tavewebsite.global.security.exception.JwtErrorMessage.INVALID_JWT_TOKEN;
import static com.tave.tavewebsite.global.security.exception.JwtErrorMessage.SIGN_OUT_USER;
import static com.tave.tavewebsite.global.security.exception.JwtErrorMessage.UNSUPPORTED_JWT_TOKEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.tavewebsite.global.exception.Response.ExceptionResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        if (request.getAttribute("signOutException") != null) {
            setResponse(response, SIGN_OUT_USER.getErrorCode(), SIGN_OUT_USER.getMessage());
        } else if (request.getAttribute("cannotUseRefreshToken") != null) {
            setResponse(response, CANNOT_USE_REFRESHTOKEN.getErrorCode(), CANNOT_USE_REFRESHTOKEN.getMessage());
        } else if (request.getAttribute("invalidJwtToken") != null) {
            setResponse(response, INVALID_JWT_TOKEN.getErrorCode(), INVALID_JWT_TOKEN.getMessage());
        } else if (request.getAttribute("expiredJwtToken") != null) {
            setResponse(response, EXPIRED_JWT_TOKEN.getErrorCode(), EXPIRED_JWT_TOKEN.getMessage());
        } else if (request.getAttribute("unsupportedJwtToken") != null) {
            setResponse(response, UNSUPPORTED_JWT_TOKEN.getErrorCode(), UNSUPPORTED_JWT_TOKEN.getMessage());
        }


    }

    // 발생한 예외에 맞게 status를 설정하고 message를 반환
    private void setResponse(HttpServletResponse response, int code, String message) throws IOException {
        response.setStatus(code);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = new ObjectMapper().writeValueAsString(ExceptionResponse.fail(code, message));
        response.getWriter().write(json);
    }

}
