package com.tave.tavewebsite.global.security.exception;

import static com.tave.tavewebsite.global.security.exception.JwtErrorMessage.CANNOT_USE_REFRESH_TOKEN;
import static com.tave.tavewebsite.global.security.exception.JwtErrorMessage.INVALID_JWT_TOKEN;
import static com.tave.tavewebsite.global.security.exception.JwtErrorMessage.NEED_ACCESS_TOKEN_REFRESH;
import static com.tave.tavewebsite.global.security.exception.JwtErrorMessage.SIGN_OUT_USER;
import static com.tave.tavewebsite.global.security.exception.JwtErrorMessage.UNSUPPORTED_JWT_TOKEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.tavewebsite.global.exception.Response.ExceptionResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        if (request.getAttribute("signOutException") != null) {
            setResponse(response, SIGN_OUT_USER.getErrorCode(), SIGN_OUT_USER.getMessage());
        } else if (request.getAttribute("cannotUseRefreshToken") != null) {
            setResponse(response, CANNOT_USE_REFRESH_TOKEN.getErrorCode(), CANNOT_USE_REFRESH_TOKEN.getMessage());
        } else if (request.getAttribute("invalidJwtToken") != null) {
            setResponse(response, INVALID_JWT_TOKEN.getErrorCode(), INVALID_JWT_TOKEN.getMessage());
        } else if (request.getAttribute("expiredJwtToken") != null) {
            setResponse(response, NEED_ACCESS_TOKEN_REFRESH.getErrorCode(), NEED_ACCESS_TOKEN_REFRESH.getMessage());
        } else if (request.getAttribute("notExistAccessToken") != null) {
            setResponse(response, NEED_ACCESS_TOKEN_REFRESH.getErrorCode(), NEED_ACCESS_TOKEN_REFRESH.getMessage());
        } else if (request.getAttribute("unsupportedJwtToken") != null) {
            setResponse(response, UNSUPPORTED_JWT_TOKEN.getErrorCode(), UNSUPPORTED_JWT_TOKEN.getMessage());
        } else {
            // 나머지 잘못된 요청에 대한 기본 예외 처리
            setResponse(response, 401, "잘못된 인증 요청입니다."); // 기본적으로 401 상태 코드와 에러 메시지 반환
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
