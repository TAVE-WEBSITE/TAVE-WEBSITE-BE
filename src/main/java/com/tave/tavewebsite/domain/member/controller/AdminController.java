package com.tave.tavewebsite.domain.member.controller;

import com.tave.tavewebsite.domain.member.dto.response.AuthorizedManagerResponseDto;
import com.tave.tavewebsite.domain.member.dto.response.UnauthorizedManagerResponseDto;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.entity.RoleType;
import com.tave.tavewebsite.domain.member.exception.NotManagerAccessException;
import com.tave.tavewebsite.domain.member.service.MemberService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;

    @GetMapping("/unauthorized")
    public SuccessResponse<List<UnauthorizedManagerResponseDto>> getUnauthorizedManager() {
        List<UnauthorizedManagerResponseDto> response = memberService.getUnauthorizedManager();
        return new SuccessResponse<>(response, SuccessMessage.UNAUTHORIZED_MEMBER_READ.getMessage());
    }

    @GetMapping("/authorized")
    public SuccessResponse<List<AuthorizedManagerResponseDto>> getAuthorizedAdmins() {
        List<AuthorizedManagerResponseDto> response = memberService.getAuthorizedAdmins();
        return new SuccessResponse<>(response, SuccessMessage.AUTHORIZED_MEMBER_READ.getMessage());
    }

    @DeleteMapping("/{memberId}")
    @PreAuthorize("hasRole('ADMIN')")
    public SuccessResponse<Void> deleteManager(@PathVariable Long memberId) {
        Member member = memberService.findMemberById(memberId);

        if (!member.getRole().equals(RoleType.MANAGER)) {
            throw new NotManagerAccessException();
        }

        memberService.deleteManager(memberId);
        return new SuccessResponse<>(null, SuccessMessage.MANAGER_DELETE.getMessage());
    }

}
