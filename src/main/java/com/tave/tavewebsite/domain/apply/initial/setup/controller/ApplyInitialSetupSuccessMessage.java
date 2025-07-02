package com.tave.tavewebsite.domain.apply.initial.setup.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplyInitialSetupSuccessMessage {
    APPLY_INITIAL_SETUP_READ_SUCCESS("지원 초기 설정 조회에 성공했습니다."),
    APPLY_INITIAL_SETUP_SAVE_SUCCESS("지원 초기 설정 저장에 성공했습니다."),
    APPLY_INITIAL_SETUP_UPDATE_SUCCESS("지원 초기 설정 업데이트에 성공했습니다."),
    APPLY_INITIAL_SETUP_DELETE_SUCCESS("지원 초기 설정 삭제에 성공했습니다."),
    CHECK_APPLY_RECRUIT_END_DATE("지원 기간 여부를 확인합니다.");

    private final String message;

    public String getMessage(String generation) {
        return generation + " " + message;
    }

}
