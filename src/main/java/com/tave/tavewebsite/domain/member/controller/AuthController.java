package com.tave.tavewebsite.domain.member.controller;

import com.tave.tavewebsite.domain.member.dto.request.RefreshTokenRequestDto;
import com.tave.tavewebsite.domain.member.dto.request.RegisterManagerRequestDto;
import com.tave.tavewebsite.domain.member.dto.request.SignUpRequestDto;
import com.tave.tavewebsite.domain.member.dto.response.RefreshResponseDto;
import com.tave.tavewebsite.domain.member.dto.response.SignInResponseDto;
import com.tave.tavewebsite.domain.member.service.AuthService;
import com.tave.tavewebsite.domain.member.service.MemberService;
import com.tave.tavewebsite.global.mail.dto.MailResponseDto;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping("/signup")
    public SuccessResponse<MailResponseDto> registerManager(@RequestBody @Valid RegisterManagerRequestDto requestDto) {
        MailResponseDto response = memberService.saveMember(requestDto);
        return new SuccessResponse<>(response);
    }

    @PostMapping("/signin")
    public SuccessResponse<SignInResponseDto> signIn(@RequestBody SignUpRequestDto requestDto,
                                                     HttpServletResponse response) {
        SignInResponseDto signInResponseDto = authService.signIn(requestDto, response);
        return new SuccessResponse<>(signInResponseDto);
    }

    @PostMapping("/refresh")
    public SuccessResponse<RefreshResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto requestDto,
                                                            @CookieValue("refreshToken") String refreshToken,
                                                            HttpServletResponse response) {
        return new SuccessResponse<>(authService.reissueToken(requestDto, refreshToken, response));
    }

    @GetMapping("/signout")
    public SuccessResponse signOut(@RequestHeader("Authorization") String token) {
        authService.singOut(token);
        return SuccessResponse.ok();
    }

    @DeleteMapping("/delete/{memberId}")
    public SuccessResponse deleteMember(@PathVariable("memberId") Long memberId) {
        memberService.deleteMember(memberId);
        return SuccessResponse.ok();
    }
}
