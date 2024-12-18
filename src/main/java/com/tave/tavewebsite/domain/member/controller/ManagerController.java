package com.tave.tavewebsite.domain.member.controller;

import com.tave.tavewebsite.domain.member.dto.request.ResetPasswordReq;
import com.tave.tavewebsite.domain.member.dto.request.ValidateEmailReq;
import com.tave.tavewebsite.domain.member.dto.response.CheckNickNameResponseDto;
import com.tave.tavewebsite.domain.member.service.AdminService;
import com.tave.tavewebsite.domain.member.service.MemberService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ManagerController {

    private final MemberService memberService;
    private final AdminService adminService;

    @PostMapping("/normal/authenticate/email")
    public SuccessResponse sendEmail(@RequestBody ValidateEmailReq requestDto) {

        memberService.sendMessage(requestDto);

        return SuccessResponse.ok(MemberSuccessMessage.SEND_AUTHENTICATION_CODE.getMessage());
    }

    @GetMapping("/normal/verify/number")
    public SuccessResponse verifyNumber(@RequestBody ValidateEmailReq requestDto) {
        memberService.verityNumber(requestDto);

        return SuccessResponse.ok(MemberSuccessMessage.VERIFY_SUCCESS_MESSAGE.getMessage());
    }

    @GetMapping("/normal/upgrade/{memberId}")
    public SuccessResponse updateAuthentication(@PathVariable("memberId") String memberId) {
        adminService.updateAuthentication(memberId);

        return new SuccessResponse("update Success.");
    }

    @GetMapping("/normal/validate/{nickName}")
    public SuccessResponse<CheckNickNameResponseDto> checkNickName(@PathVariable("nickName") String nickName) {
        memberService.validateNickname(nickName);
        CheckNickNameResponseDto response = new CheckNickNameResponseDto(nickName);
        return new SuccessResponse<>(response, MemberSuccessMessage.CAN_USE_NICKNAME.getMessage(nickName));
    }

    @PutMapping("/normal/reset/password")
    public SuccessResponse resetPassword(@RequestBody ResetPasswordReq requestDto) {
        memberService.resetPassword(requestDto);

        return SuccessResponse.ok(MemberSuccessMessage.RESET_PASSWORD_MESSAGE.getMessage());
    }

}
