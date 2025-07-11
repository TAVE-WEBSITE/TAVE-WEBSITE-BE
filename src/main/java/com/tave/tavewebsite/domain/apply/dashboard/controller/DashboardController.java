package com.tave.tavewebsite.domain.apply.dashboard.controller;

import com.tave.tavewebsite.domain.apply.dashboard.dto.DashboardResDto;
import com.tave.tavewebsite.domain.apply.dashboard.service.DashboardService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tave.tavewebsite.domain.apply.dashboard.controller.DashboardSuccessMessage.DASHBOARD_SUCCESS_MESSAGE;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/admin/dashboard")
    public SuccessResponse<DashboardResDto> getDashboard() {
        return new SuccessResponse<>(dashboardService.getDashboard(), DASHBOARD_SUCCESS_MESSAGE.getMessage());
    }
}
