package com.tave.tavewebsite.global.security.filter;

import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import com.tave.tavewebsite.global.security.exception.JwtValidException.SignOutUserException;
import com.tave.tavewebsite.global.security.utils.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 1. Request Header에서 JWT 토큰 추출
        String token = resolveToken(request);

        // 2. redis를 통해 로그아웃한 사용자인지 확인
        if (redisUtil.hasKey("Bearer " + token)) {
            request.setAttribute("signOutException", 401);
            throw new SignOutUserException();
        }

        // 3. 새로고침 시 메모리에 저장된 액세스 토큰이 사라졌을 시 발생시키는 에러
        if (token == null) {
            request.setAttribute("notExistAccessToken", 401);
        }

        // 4. validateToken으로 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(request, token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
            Authentication authentication = jwtTokenProvider.getAuthentication(request, token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        // 해당 토큰 검증 필터가 적용되지 않게 하려는 api 경로
        return requestURI.startsWith("/v1/normal")
                || requestURI.startsWith("/v1/auth/signup")
                || requestURI.startsWith("/v1/auth/signin")
                || (requestURI.startsWith("/v1/auth/refresh"));

    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
