package com.tave.tavewebsite.domain.resume.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    RESUME_NOT_FOUND(400, "해당 회원의 이력서가 존재하지 않습니다."),
    MEMBER_NOT_FOUND(400, "회원이 존재하지 않습니다."),
    INVALID_FIELD(400, "지원 분야가 올바르지 않습니다.");

    private final int code;
    private final String message;
}
