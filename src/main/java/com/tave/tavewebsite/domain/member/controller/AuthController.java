package com.tave.tavewebsite.domain.member.controller;

import com.tave.tavewebsite.domain.member.dto.request.*;
import com.tave.tavewebsite.domain.member.dto.response.RefreshResponseDto;
import com.tave.tavewebsite.domain.member.dto.response.SignInResponseDto;
import com.tave.tavewebsite.domain.member.service.AuthService;
import com.tave.tavewebsite.domain.member.service.MemberService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.tave.tavewebsite.domain.member.controller.MemberSuccessMessage.MANAGER_MEMBER_SIGNUP;
import static com.tave.tavewebsite.domain.member.controller.MemberSuccessMessage.NORMAL_MEMBER_SIGNUP;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class AuthController {

    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping("/auth/signup")
    public SuccessResponse registerManager(@RequestBody @Valid RegisterManagerRequestDto requestDto) {
        memberService.saveMember(requestDto);
        return new SuccessResponse<>(MANAGER_MEMBER_SIGNUP.getMessage());
    }

    @PostMapping("/auth/normal/signup")
    public SuccessResponse registerMember(@RequestBody @Valid RegisterMemberRequestDto dto) {
        memberService.saveNormalMember(dto);

        return SuccessResponse.ok(NORMAL_MEMBER_SIGNUP.getMessage());
    }

    @PostMapping("/auth/signin")
    public SuccessResponse<SignInResponseDto> signIn(@RequestBody SignUpRequestDto requestDto,
                                                     HttpServletResponse response) {
        SignInResponseDto signInResponseDto = authService.signIn(requestDto, response);
        return new SuccessResponse<>(signInResponseDto);
    }

    @PostMapping("/auth/refresh")
    public SuccessResponse<RefreshResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto requestDto,
                                                            @CookieValue("refreshToken") String refreshToken,
                                                            HttpServletResponse response) {
        return new SuccessResponse<>(authService.reissueToken(requestDto, refreshToken, response));
    }

    @GetMapping("/auth/signout")
    public SuccessResponse signOut(@RequestHeader("Authorization") String token, HttpServletResponse response) {
        authService.signOut(token, response);
        return SuccessResponse.ok(MemberSuccessMessage.SIGN_OUT_SUCCESS.getMessage());
    }

    @DeleteMapping("/auth/delete/{memberId}")
    public SuccessResponse deleteMember(@PathVariable("memberId") Long memberId) {
        memberService.deleteMember(memberId);
        return SuccessResponse.ok(MemberSuccessMessage.DELETE_MEMBER_SUCCESS.getMessage());
    }

    @PostMapping("/normal/reset/verify")
    public SuccessResponse sendPasswordResetCode(@RequestBody ResetPasswordVerifyRequestDto requestDto) {
        memberService.validateMemberInfoAndSendVerificationCode(requestDto);
        return SuccessResponse.ok(MemberSuccessMessage.SEND_AUTHENTICATION_CODE.getMessage());
    }

    @PostMapping("/normal/reset/verify/code")
    public SuccessResponse verifyResetCode(@RequestParam String email, @RequestParam String code) {
        memberService.verifyAuthCodeForPasswordReset(email, code);
        return SuccessResponse.ok(MemberSuccessMessage.VERIFY_SUCCESS.getMessage());
    }

    @PostMapping("/normal/password/change")
    public SuccessResponse resetNormalPassword(@RequestBody @Valid ResetNormalPasswordRequestDto requestDto) {
        memberService.resetNormalMemberPassword(requestDto);
        return SuccessResponse.ok(MemberSuccessMessage.RESET_PASSWORD.getMessage());
    }
}
