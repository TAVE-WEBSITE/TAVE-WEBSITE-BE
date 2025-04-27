package com.tave.tavewebsite.domain.programinglaunguage.controller;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProgramingLanguageSuccessMessage {
    UPDATE_SUCCESS("레벨 수정에 성공했습니다."),
    READ_LEVEL_SUCCESS("레벨 조회에 성공했습니다."),
    READ_LANGUAGE_SUCCESS("모든 언어 조회에 성공했습니다."),
    CREATE_LANGUAGE_SUCCESS("언어 생성에 성공했습니다."),
    DELETE_LANGUAGE_SUCCESS("언어 삭제에 성공했습니다.");

    private final String message;

    public String getMessage() {
        return message;
    }
}

