package com.tave.tavewebsite.domain.member.service;

import com.tave.tavewebsite.domain.member.dto.request.RefreshTokenRequestDto;
import com.tave.tavewebsite.domain.member.dto.request.SignUpRequestDto;
import com.tave.tavewebsite.domain.member.dto.response.RefreshResponseDto;
import com.tave.tavewebsite.domain.member.dto.response.SignInResponseDto;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.exception.NotFoundMemberException;
import com.tave.tavewebsite.domain.member.memberRepository.MemberRepository;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeState;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import com.tave.tavewebsite.global.security.entity.JwtToken;
import com.tave.tavewebsite.global.security.exception.JwtValidException.NotMatchRefreshTokenException;
import com.tave.tavewebsite.global.security.utils.CookieUtil;
import com.tave.tavewebsite.global.security.utils.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final ResumeRepository resumeRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    private static final int REFRESH_TOKEN_MAX_AGE = 60 * 24 * 3;
    private static final int ACCESS_TOKEN_MAX_AGE = 30;

    public SignInResponseDto signIn(SignUpRequestDto requestDto, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                requestDto.email(),
                requestDto.password());
        authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtToken jwtToken = generateToken(requestDto.email());
        CookieUtil.setCookie(response, "refreshToken", jwtToken.getRefreshToken());

        Member member = memberRepository.findByEmail(requestDto.email())
                .orElseThrow(NotFoundMemberException::new);

        ResumeState resumeState = resumeRepository.findByMemberId(member.getId())
                .map(Resume::getState)
                .orElse(ResumeState.TEMPORARY);
        return SignInResponseDto.from(jwtToken, member, resumeState);
    }

    public void signOut(String accessToken, HttpServletResponse response) {
        redisUtil.set(accessToken, "ban accessToken", ACCESS_TOKEN_MAX_AGE);
        CookieUtil.deleteCookie(response, "refreshToken");
    }

    public RefreshResponseDto reissueToken(RefreshTokenRequestDto requestDto, String refreshToken,
                                           HttpServletResponse response) {
        Object refreshTokenByRedis = redisUtil.get(requestDto.email());
        if (!refreshTokenByRedis.equals(refreshToken)) {
            throw new NotMatchRefreshTokenException();
        }

        JwtToken jwtToken = generateToken(requestDto.email());
        CookieUtil.setCookie(response, "refreshToken", jwtToken.getRefreshToken());
        return RefreshResponseDto.from(jwtToken);
    }

    private JwtToken generateToken(String email) {
        JwtToken jwtToken = jwtTokenProvider.generateToken(
                memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new));
        redisUtil.set(email, jwtToken.getRefreshToken(), REFRESH_TOKEN_MAX_AGE);

        return jwtToken;
    }
}
