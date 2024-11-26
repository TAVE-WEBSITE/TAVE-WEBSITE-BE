package com.tave.tavewebsite.domain.member.controller;

import com.tave.tavewebsite.domain.member.dto.request.RefreshTokenRequestDto;
import com.tave.tavewebsite.domain.member.dto.request.RegisterManagerRequestDto;
import com.tave.tavewebsite.domain.member.dto.request.SignUpRequestDto;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final MemberService memberService;

    @PostMapping
    public SuccessResponse<MailResponseDto> registerManager(@RequestBody @Valid RegisterManagerRequestDto requestDto) {
        MailResponseDto response = memberService.saveMember(requestDto);
        return new SuccessResponse<>(response);
    }

    @PostMapping("/signIn")
    public SuccessResponse<SignInResponseDto> signIn(@RequestBody SignUpRequestDto requestDto) {
        SignInResponseDto signInResponseDto = memberService.signIn(requestDto);
        return new SuccessResponse<>(signInResponseDto);
    }

    @PostMapping("/refresh")
    public SuccessResponse<JwtToken> refreshToken(@RequestBody RefreshTokenRequestDto requestDto) {
        JwtToken jwtToken = memberService.refreshToken(requestDto);
        return new SuccessResponse<>(jwtToken);
    }

    @GetMapping("/signOut")
    public SuccessResponse signOut(@RequestHeader("Authorization") String token) {
        memberService.singOut(token);
        return SuccessResponse.ok();
    }

    @GetMapping("/unauthorized")
    public SuccessResponse<List<UnauthorizedManagerResponseDto>> getUnauthorizedManager() {
        List<UnauthorizedManagerResponseDto> response = memberService.getUnauthorizedManager();
        return new SuccessResponse<>(response);
    }

    @GetMapping("/{nickName}")
    public SuccessResponse<CheckNickNameResponseDto> checkNickName(@PathVariable("nickName") String nickName) {
        memberService.validateNickname(nickName);
        CheckNickNameResponseDto response = new CheckNickNameResponseDto(nickName);
        return new SuccessResponse<>(response, nickName + " 사용가능합니다.");
    }

    @DeleteMapping("/{memberId}")
    public SuccessResponse deleteMember(@PathVariable("memberId") Long memberId) {
        memberService.deleteMember(memberId);
        return SuccessResponse.ok();
    }

    // ci/cd 이후 배포 성공 테스트용 엔드포인트
    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
