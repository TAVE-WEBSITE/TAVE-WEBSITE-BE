package com.tave.tavewebsite.domain.resume.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PersonalInfoSuccessMessage {
    CREATE_SUCCESS("개인정보 작성에 성공했습니다."),
    UPDATE_SUCCESS("개인정보 수정에 성공했습니다."),
    READ_SUCCESS("개인정보 조회에 성공했습니다."),
    DELETE_SUCCESS("개인정보 삭제에 성공했습니다."),
    READ_INTERVIEW_SUCCESS("면접 시간표 조회에 성공했습니다."),
    QUESTION_READ_SUCCESS("질문과 답변 조회에 성공했습니다."),
    TEMP_SAVE_SUCCESS("임시 저장에 성공했습니다."),
    TEMP_LOAD_SUCCESS("임시 저장 조회에 성공했습니다."),
    ANSWER_CREATE_SUCCESS("지원서에 대한 답변이 저장되었습니다."),
    SUBMIT_SUCCESS("지원서가 최종 제출되었습니다."),
    RESUME_EMAIL_SEND_SUCCESS("지원서 제출 완료 메일 전송에 성공하였습니다.");

    private final String message;

}