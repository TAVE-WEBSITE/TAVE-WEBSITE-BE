package com.tave.tavewebsite.domain.resume.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    // ResumeQuestion
    RESUME_QUESTION_NOT_MATCH_RESUME(400, "해당 Resume의 ResumeQuestion이 없습니다."),
    RESUME_ANSWER_TEXT_LENGTH_OVER(400, "답변 길이를 초과했습니다."),

    // Resume
    RESUME_NOT_FOUND(404, "해당 이력서가 존재하지 않습니다."),
    MEMBER_NOT_FOUND(400, "회원이 존재하지 않습니다."),

    UNAUTHORIZED_RESUME(403, "해당 이력서를 수정할 권한이 없습니다."),
    NOT_VALID_TIME(400, "올바르지 않은 형식의 시간대입니다."),

    SOCIAL_LINK_NOT_FOUND(404, "url이 존재하지 않습니다."),

    FIELD_TYPE_INVALID(400, "유효하지 않은 필드 타입입니다."),

    INVALID_PAGE_NUMBER(400, "유효하지 않은 페이지 번호입니다."),
    ALREADY_SUBMITTED(400, "이미 제출된 이력서입니다."),

    // Redis 임시 저장
    TEMP_NOT_FOUND(404, "임시 저장된 정보가 없습니다."),
    TEMP_PARSE_FAILED(500, "임시 정보를 파싱하는 데 실패했습니다."),
    TEMP_SERIALIZE_FAILED(500, "임시 저장 데이터를 JSON으로 변환하는 데 실패했습니다."),
    TEMP_SAVE_FAILED(500, "임시 저장에 실패했습니다."),

    // Resume Evaluation
    ALREADY_EXISTS_EVALUATION(400, "이미 평가중인 이력서입니다.");

    private final int code;
    private final String message;
}
