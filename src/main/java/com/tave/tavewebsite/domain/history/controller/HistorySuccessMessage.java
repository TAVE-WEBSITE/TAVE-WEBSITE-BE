package com.tave.tavewebsite.domain.history.controller;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum HistorySuccessMessage {
    POST_SUCCESS_MESSAGE("이력 등록에 성공했습니다."),
    UPDATE_SUCCESS_MESSAGE("이력 수정에 성공했습니다."),
    DELETE_SUCCESS_MESSAGE("이력 삭제에 성공했습니다.");

    private final String message;

    public String getMessage(String generation) {
        return generation + " " + message;
    }

    public String getMessage() {
        return message;
    }
}
