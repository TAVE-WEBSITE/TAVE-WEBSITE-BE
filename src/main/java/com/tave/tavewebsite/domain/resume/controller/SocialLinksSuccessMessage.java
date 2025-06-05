package com.tave.tavewebsite.domain.resume.controller;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SocialLinksSuccessMessage {
    CREATE_SUCCESS("url 작성에 성공했습니다."),
    UPDATE_SUCCESS("url 수정에 성공했습니다."),
    READ_SUCCESS("url 조회에 성공했습니다."),
    DELETE_SUCCESS("url 삭제에 성공했습니다."),
    UPLOAD_SUCCESS("포트폴리오 업로드에 성공했습니다."),
    TEMP_SAVE_SUCCESS("소셜 링크 임시 저장에 성공했습니다."),
    TEMP_READ_SUCCESS("소셜 링크 임시 조회에 성공했습니다."),
    PORTFOLIO_TEMP_SAVE_SUCCESS("포트폴리오 임시 저장에 성공했습니다."),
    PORTFOLIO_TEMP_READ_SUCCESS("포트폴리오 임시 조회에 성공했습니다.");

    private final String message;

    public String getMessage() {
        return message;
    }
}
