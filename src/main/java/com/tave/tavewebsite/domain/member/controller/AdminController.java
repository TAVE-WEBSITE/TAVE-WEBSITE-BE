package com.tave.tavewebsite.domain.member.controller;

import com.tave.tavewebsite.domain.member.dto.response.AuthorizedManagerResponseDto;
import com.tave.tavewebsite.domain.member.dto.response.UnauthorizedManagerResponseDto;
import com.tave.tavewebsite.domain.member.service.AdminService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/unauthorized")
    public SuccessResponse<List<UnauthorizedManagerResponseDto>> getUnauthorizedManager() {
        List<UnauthorizedManagerResponseDto> response = adminService.getUnauthorizedManager();
        return new SuccessResponse<>(response, MemberSuccessMessage.UNAUTHORIZED_MEMBER_READ.getMessage());
    }

    @GetMapping("/authorized")
    public SuccessResponse<List<AuthorizedManagerResponseDto>> getAuthorizedAdmins() {
        List<AuthorizedManagerResponseDto> response = adminService.getAuthorizedAdmins();
        return new SuccessResponse<>(response, MemberSuccessMessage.AUTHORIZED_MEMBER_READ.getMessage());
    }

    @DeleteMapping("/{memberId}")
    public SuccessResponse deleteManager(@PathVariable Long memberId) {
        adminService.deleteManager(memberId);
        return SuccessResponse.ok(MemberSuccessMessage.MANAGER_DELETE.getMessage());
    }

}
