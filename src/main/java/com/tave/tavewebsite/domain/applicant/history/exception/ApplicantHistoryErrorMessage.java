package com.tave.tavewebsite.domain.applicant.history.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicantHistoryErrorMessage {

    NOT_FOUND_APPLICANT_HISTORY(400, "지원 이력을 찾을 수 없습니다.");

    final int code;
    final String message;

}
