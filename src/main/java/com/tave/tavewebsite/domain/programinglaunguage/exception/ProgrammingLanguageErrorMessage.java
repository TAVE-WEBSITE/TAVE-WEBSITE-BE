package com.tave.tavewebsite.domain.programinglaunguage.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProgrammingLanguageErrorMessage {

    NOT_FOUND_PROGRAMMING_LANGUAGE(400, "해당 언어를 찾을 수 없습니다."),
    NOT_FOUND_FIELD_LANGUAGE(400, "해당 언어를 찾을 수 없습니다.");


    final int code;
    final String message;

}