package com.tave.tavewebsite.domain.member.controller;

import com.tave.tavewebsite.domain.member.dto.response.AuthorizedManagerResponseDto;
import com.tave.tavewebsite.domain.member.dto.response.UnauthorizedManagerResponseDto;
import com.tave.tavewebsite.domain.member.service.MemberService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;

    @GetMapping("/unauthorized")
    public SuccessResponse<List<UnauthorizedManagerResponseDto>> getUnauthorizedManager() {
        List<UnauthorizedManagerResponseDto> response = memberService.getUnauthorizedManager();
        return new SuccessResponse<>(response);
    }

    @GetMapping("/authorized")
    public SuccessResponse<List<AuthorizedManagerResponseDto>> getAuthorizedAdmins() {
        List<AuthorizedManagerResponseDto> response = memberService.getAuthorizedAdmins();
        return new SuccessResponse<>(response);
    }
}
