package com.tave.tavewebsite.domain.programinglaunguage.controller;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProgramingLanguageSuccessMessage {
    UPDATE_SUCCESS("레벨 수정에 성공했습니다."),
    READ_SUCCESS("레벨 조회에 성공했습니다.");

    private final String message;

    public String getMessage() {
        return message;
    }
}

