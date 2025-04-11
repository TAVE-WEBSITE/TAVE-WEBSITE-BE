package com.tave.tavewebsite.domain.member.controller;

import com.tave.tavewebsite.domain.member.dto.request.*;
import com.tave.tavewebsite.domain.member.dto.response.RefreshResponseDto;
import com.tave.tavewebsite.domain.member.dto.response.SignInResponseDto;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.service.AuthService;
import com.tave.tavewebsite.domain.member.service.MemberService;
import com.tave.tavewebsite.global.mail.dto.MailResponseDto;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.tave.tavewebsite.domain.member.controller.MemberSuccessMessage.NORMAL_MEMBER_SIGNUP;

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

    @PostMapping("/normal/signup")
    public SuccessResponse registerMember(@RequestBody @Valid RegisterMemberRequestDto dto) {
        memberService.saveNormalMember(dto);

        return SuccessResponse.ok(NORMAL_MEMBER_SIGNUP.getMessage());
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
    public SuccessResponse signOut(@RequestHeader("Authorization") String token, HttpServletResponse response) {
        authService.singOut(token, response);
        return SuccessResponse.ok(MemberSuccessMessage.SIGN_OUT_SUCCESS.getMessage());
    }

    @DeleteMapping("/delete/{memberId}")
    public SuccessResponse deleteMember(@PathVariable("memberId") Long memberId) {
        memberService.deleteMember(memberId);
        return SuccessResponse.ok(MemberSuccessMessage.DELETE_MEMBER_SUCCESS.getMessage());
    }

    @PostMapping("/normal/reset/verify")
    public SuccessResponse sendPasswordResetCode(@RequestBody ResetPasswordVerifyRequestDto requestDto) {
        memberService.verifyNormalMemberForPasswordReset(requestDto);
        return SuccessResponse.ok(MemberSuccessMessage.SEND_AUTHENTICATION_CODE.getMessage());
    }
}
