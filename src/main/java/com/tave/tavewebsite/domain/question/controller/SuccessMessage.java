package com.tave.tavewebsite.domain.question.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessMessage {

    QUESTION_CREATED("지원서 질문을 성공적으로 생성했습니다."),
    QUESTION_UPDATED("지원서 질문을 수정했습니다."),
    QUESTION_ORDERED_SWAP("두 질문 순서를 바꿨습니다."),
    QUESTION_DELETED("지원서 질문을 삭제했습니다."),
    QUESTION_FIELD_LIST("특정 분야 지원서 질문 목록을 반환합니다."),
    QUESTION_ALL_LIST("모든 지원서 질문 목록을 반환합니다.");

    private final String message;

}
