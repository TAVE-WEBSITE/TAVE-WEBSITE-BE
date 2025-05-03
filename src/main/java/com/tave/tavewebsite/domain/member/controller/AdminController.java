package com.tave.tavewebsite.domain.member.controller;

import com.tave.tavewebsite.domain.member.dto.response.ManagerResponseDto;
import com.tave.tavewebsite.domain.member.service.AdminService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 승인 완료 운영진 조회
    @PostMapping("/manager/authorized")
    public SuccessResponse<Page<ManagerResponseDto>> getAuthorizedManagers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        return adminService.getManagersByStatus("AUTHORIZED", pageable);
    }

    // 승인 대기 운영진 조회
    @PostMapping("/manager/unauthorized")
    public SuccessResponse<Page<ManagerResponseDto>> getUnauthorizedManagers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        return adminService.getManagersByStatus("UNAUTHORIZED", pageable);
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
