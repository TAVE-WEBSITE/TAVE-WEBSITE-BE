package com.tave.tavewebsite.domain.applicant.history.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationStatus {
    DRAFT("Draft", "작성 중"),
    SUBMITTED("Submitted", "지원 완료"),
    DOCUMENT_PASSED("Document Passed", "서류 합격"),
    REJECTED("Rejected", "서류 불합격"),
    FINAL_FAIL("Final Rejected","면접 불합격"),
    FINAL_ACCEPTED("Final Accepted", "최종 합격");

    private final String message;
    private final String displayName;
}
