package com.tave.tavewebsite.domain.member.controller;

import com.tave.tavewebsite.domain.member.dto.request.RefreshTokenRequestDto;
import com.tave.tavewebsite.domain.member.dto.request.RegisterManagerRequestDto;
import com.tave.tavewebsite.domain.member.dto.request.SignUpRequestDto;
import com.tave.tavewebsite.domain.member.dto.response.SignInResponseDto;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.service.MemberService;
import com.tave.tavewebsite.global.mail.dto.MailResponseDto;
import com.tave.tavewebsite.global.security.entity.JwtToken;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/auth/signup")
    public SuccessResponse<MailResponseDto> registerManager(@RequestBody @Valid RegisterManagerRequestDto requestDto) {
        MailResponseDto response = memberService.saveMember(requestDto);
        return new SuccessResponse<>(response);
    }

    @PostMapping("/auth/signin")
    public SuccessResponse<SignInResponseDto> signIn(@RequestBody SignUpRequestDto requestDto) {
        SignInResponseDto signInResponseDto = memberService.signIn(requestDto);
        return new SuccessResponse<>(signInResponseDto);
    }

    @PostMapping("/auth/refresh")
    public SuccessResponse<JwtToken> refreshToken(@RequestBody RefreshTokenRequestDto requestDto) {
        JwtToken jwtToken = memberService.refreshToken(requestDto);
        return new SuccessResponse<>(jwtToken);
    }

    @GetMapping("/auth/signout")
    public SuccessResponse signOut(@RequestHeader("Authorization") String token) {
        memberService.singOut(token);
        return SuccessResponse.ok();
    }

    @DeleteMapping("/auth/delete/{memberId}")
    public SuccessResponse deleteMember(@PathVariable("memberId") Long memberId) {
        memberService.deleteMember(memberId);
        return SuccessResponse.ok();
    }
}
