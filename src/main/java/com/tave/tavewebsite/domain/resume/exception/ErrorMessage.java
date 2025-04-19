package com.tave.tavewebsite.domain.resume.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    // ResumeQuestion
    RESUME_QUESTION_NOT_MATCH_RESUME(400, "해당 Resume의 ResumeQuestion이 없습니다."),

    // Resume
    RESUME_NOT_FOUND(404, "해당 이력서가 존재하지 않습니다."),
    MEMBER_NOT_FOUND(400, "회원이 존재하지 않습니다."),

    UNAUTHORIZED_RESUME(403, "해당 이력서를 수정할 권한이 없습니다."),
    NOT_VALID_TIME(400, "올바르지 않은 형식의 시간대입니다."),

    SOCIAL_LINK_NOT_FOUND(404, "url이 존재하지 않습니다.");
    private final int code;
    private final String message;
}
