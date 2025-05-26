package com.tave.tavewebsite.domain.apply.initial.setup.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplyInitialSetupErrorMessage {

    NOT_FOUND_APPLY_INITIAL_SETUP(400, "지원 초기 설정을 찾을 수 없습니다.");

    final int code;
    final String message;

}
