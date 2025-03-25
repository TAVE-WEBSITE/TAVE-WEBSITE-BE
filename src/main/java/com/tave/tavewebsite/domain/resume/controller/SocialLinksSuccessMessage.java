package com.tave.tavewebsite.domain.resume.controller;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SocialLinksSuccessMessage {
    CREATE_SUCCESS("url 작성에 성공했습니다."),
    UPDATE_SUCCESS("url 수정에 성공했습니다."),
    READ_SUCCESS("url 조회에 성공했습니다."),
    DELETE_SUCCESS("url 삭제에 성공했습니다.");

    private final String message;

    public String getMessage() {
        return message;
    }
}
