package com.tave.tavewebsite.domain.apply.dashboard.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DashboardErrorMessage {

    NOT_FOUND_DASHBOARD(404, "대시보드 조회를 불러올 수 없습니다.");

    final int code;
    final String message;
}
