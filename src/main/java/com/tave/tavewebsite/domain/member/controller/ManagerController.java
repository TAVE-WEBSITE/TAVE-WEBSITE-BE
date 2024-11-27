package com.tave.tavewebsite.domain.member.controller;

import com.tave.tavewebsite.domain.member.dto.request.*;
import com.tave.tavewebsite.domain.member.dto.response.CheckNickNameResponseDto;
import com.tave.tavewebsite.domain.member.dto.response.SignInResponseDto;
import com.tave.tavewebsite.domain.member.dto.response.UnauthorizedManagerResponseDto;
import com.tave.tavewebsite.domain.member.service.MemberService;
import com.tave.tavewebsite.global.mail.dto.MailResponseDto;
import com.tave.tavewebsite.global.security.entity.JwtToken;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ManagerController {

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

    @GetMapping("/admin/unauthorized")
    public SuccessResponse<List<UnauthorizedManagerResponseDto>> getUnauthorizedManager() {
        List<UnauthorizedManagerResponseDto> response = memberService.getUnauthorizedManager();
        return new SuccessResponse<>(response);
    }

    @GetMapping("/normal/validate/{nickName}")
    public SuccessResponse<CheckNickNameResponseDto> checkNickName(@PathVariable("nickName") String nickName) {
        memberService.validateNickname(nickName);
        CheckNickNameResponseDto response = new CheckNickNameResponseDto(nickName);
        return new SuccessResponse<>(response, nickName + " 사용가능합니다.");
    }

    @DeleteMapping("/auth/delete/{memberId}")
    public SuccessResponse deleteMember(@PathVariable("memberId") Long memberId) {
        memberService.deleteMember(memberId);
        return SuccessResponse.ok();
    }

    // ci/cd 이후 배포 성공 테스트용 엔드포인트
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/normal/authenticate/email")
    public SuccessResponse sendEmail(@RequestBody ValidateEmailReq requestDto) {

        memberService.sendMessage(requestDto);

        return SuccessResponse.ok("이메일로 인증 번호가 전송되었습니다!");
    }

    @GetMapping("/normal/verify/number")
    public SuccessResponse verifyNumber(@RequestBody ValidateEmailReq requestDto) {
        memberService.verityNumber(requestDto);

        return SuccessResponse.ok("인증되었습니다!");
    }

    @PutMapping("/normal/reset/password")
    public SuccessResponse resetPassword(@RequestBody ResetPasswordReq requestDto) {
        memberService.resetPassword(requestDto);

        return SuccessResponse.ok("비밀번호가 재설정되었습니다.\n 다시 로그인해주세요!");
    }
}
