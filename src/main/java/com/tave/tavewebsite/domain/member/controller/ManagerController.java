package com.tave.tavewebsite.domain.member.controller;

import com.tave.tavewebsite.domain.member.dto.request.RegisterManagerRequestDto;
import com.tave.tavewebsite.domain.member.dto.response.UnauthorizedManagerResponseDto;
import com.tave.tavewebsite.domain.member.service.MemberService;
import com.tave.tavewebsite.global.mail.dto.MailResponseDto;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
