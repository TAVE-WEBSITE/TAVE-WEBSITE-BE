package com.tave.tavewebsite.domain.member.controller;

import com.tave.tavewebsite.domain.member.dto.response.ManagerResponseDto;
import com.tave.tavewebsite.domain.member.service.AdminService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/manager")
    public SuccessResponse<Page<ManagerResponseDto>> getManagers(
            @RequestParam(defaultValue = "ALL", name = "status") String status,
            @PageableDefault(size = 8) Pageable pageable) {
        return adminService.getManagersByStatus(status, pageable);
    }

    @DeleteMapping("/{memberId}")
    public SuccessResponse deleteManager(@PathVariable Long memberId) {
        adminService.deleteManager(memberId);
        return SuccessResponse.ok(MemberSuccessMessage.MANAGER_DELETE.getMessage());
    }

    @PatchMapping("/unauthorized-manager/{memberId}")
    public SuccessResponse approveManager(@PathVariable Long memberId) {
        adminService.approveManager(memberId);
        return SuccessResponse.ok(MemberSuccessMessage.MANAGER_APPROVED.getMessage());
    }

    @DeleteMapping("/unauthorized-manager/{memberId}")
    public SuccessResponse rejectManager(@PathVariable Long memberId) {
        adminService.rejectManager(memberId);
        return SuccessResponse.ok(MemberSuccessMessage.MANAGER_REJECTED.getMessage());
    }

}
