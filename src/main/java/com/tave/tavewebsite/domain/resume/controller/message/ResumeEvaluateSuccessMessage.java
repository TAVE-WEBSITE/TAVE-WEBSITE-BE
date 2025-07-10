package com.tave.tavewebsite.domain.resume.controller.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResumeEvaluateSuccessMessage {

    CREATE_FINAL_STATUS_SUCCESS("최종 서류 평가가 업데이트되었습니다."),
    CREATE_SUCCESS("평가 업데이트에 성공했습니다."),
    UPDATE_SUCCESS("평가 수정에 성공했습니다."),
    READ_SUCCESS("평가 조회에 성공했습니다."),
    DELETE_SUCCESS("평가 삭제에 성공했습니다.");

    private final String message;
}
