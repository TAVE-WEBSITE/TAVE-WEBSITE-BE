package com.tave.tavewebsite.domain.apply.dashboard.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DashboardSuccessMessage {

    DASHBOARD_SUCCESS_MESSAGE("대시보드를 성공적으로 조회하였습니다."),
    DASHBOARD_UPDATE_SUCCESS("대시보드를 성공적으로 업데이트하였습니다.");

    private final String message;
}
