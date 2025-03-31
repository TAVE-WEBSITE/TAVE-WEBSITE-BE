package com.tave.tavewebsite.domain.member.controller;

import com.tave.tavewebsite.domain.member.dto.request.RegisterMemberRequestDto;
import com.tave.tavewebsite.domain.member.dto.request.ResetPasswordReq;
import com.tave.tavewebsite.domain.member.dto.request.ValidateEmailReq;
import com.tave.tavewebsite.domain.member.dto.response.CheckNickNameResponseDto;
import com.tave.tavewebsite.domain.member.service.AdminService;
import com.tave.tavewebsite.domain.member.service.MemberService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.tave.tavewebsite.domain.member.controller.MemberSuccessMessage.NORMAL_MEMBER_SIGNUP;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ManagerController {

    private final MemberService memberService;
    private final AdminService adminService;

    @PostMapping("/normal/signup")
    public SuccessResponse registerMember(@RequestBody @Valid RegisterMemberRequestDto dto){
        memberService.saveNormalMember(dto);

        return SuccessResponse.ok(NORMAL_MEMBER_SIGNUP.getMessage());
    }

    @PostMapping("/normal/authenticate/email")
    public SuccessResponse sendEmail(@RequestBody ValidateEmailReq requestDto,
                                     @RequestParam(required = false, defaultValue = "false") Boolean reset) {

        memberService.sendMessage(requestDto, reset);

        return SuccessResponse.ok(MemberSuccessMessage.SEND_AUTHENTICATION_CODE.getMessage());
    }

    @PostMapping("/normal/verify/number")
    public SuccessResponse verifyNumber(@RequestBody ValidateEmailReq requestDto) {
        memberService.verityNumber(requestDto);

        return SuccessResponse.ok(MemberSuccessMessage.VERIFY_SUCCESS.getMessage());
    }

    @GetMapping("/normal/upgrade/{memberId}")
    public SuccessResponse updateAuthentication(@PathVariable("memberId") String memberId) {
        adminService.updateAuthentication(memberId);

        return new SuccessResponse("테스트용 update Success.");
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

        return SuccessResponse.ok(MemberSuccessMessage.RESET_PASSWORD.getMessage());
    }

}
