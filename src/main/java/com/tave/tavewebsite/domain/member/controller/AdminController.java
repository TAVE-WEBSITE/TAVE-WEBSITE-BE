package com.tave.tavewebsite.domain.member.controller;

import com.tave.tavewebsite.domain.member.dto.response.AuthorizedManagerResponseDto;
import com.tave.tavewebsite.domain.member.dto.response.UnauthorizedManagerResponseDto;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.entity.RoleType;
import com.tave.tavewebsite.domain.member.exception.NotManagerAccessException;
import com.tave.tavewebsite.domain.member.service.AdminService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.tave.tavewebsite.domain.member.controller.SuccessMessage.UNAUTHORIZED_MEMBER_READ;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/unauthorized")
    public SuccessResponse<List<UnauthorizedManagerResponseDto>> getUnauthorizedManager() {
       adminService.getUnauthorizedManager();
       return SuccessResponse.ok(SuccessMessage.UNAUTHORIZED_MEMBER_READ.getMessage());
    }

    @GetMapping("/authorized")
    public SuccessResponse<List<AuthorizedManagerResponseDto>> getAuthorizedAdmins() {
        adminService.getAuthorizedAdmins();
        return SuccessResponse.ok(SuccessMessage.AUTHORIZED_MEMBER_READ.getMessage());
    }

    @DeleteMapping("/{memberId}")
    public SuccessResponse deleteManager(@PathVariable Long memberId) {
        adminService.deleteManager(memberId);
        return SuccessResponse.ok(SuccessMessage.MANAGER_DELETE.getMessage());
    }

}
