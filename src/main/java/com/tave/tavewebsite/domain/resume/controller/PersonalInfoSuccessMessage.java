package com.tave.tavewebsite.domain.resume.controller;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PersonalInfoSuccessMessage {
    CREATE_SUCCESS("개인정보 작성에 성공했습니다."),
    UPDATE_SUCCESS("개인정보 수정에 성공했습니다."),
    READ_SUCCESS("개인정보 조회에 성공했습니다."),
    DELETE_SUCCESS("개인정보 삭제에 성공했습니다."),
    QUESTION_READ_SUCCESS("질문과 답변 조회에 성공했습니다."),
    TEMP_SAVE_SUCCESS("임시 저장에 성공했습니다."),
    TEMP_LOAD_SUCCESS("임시 저장 조회에 성공했습니다.");

    private final String message;

    public String getMessage() {
        return message;
    }
}