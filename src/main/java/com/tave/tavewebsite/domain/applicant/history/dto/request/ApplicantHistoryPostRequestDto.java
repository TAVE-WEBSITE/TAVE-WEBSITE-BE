package com.tave.tavewebsite.domain.applicant.history.dto.request;

import com.tave.tavewebsite.domain.applicant.history.entity.ApplicationStatus;
import com.tave.tavewebsite.global.common.FieldType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ApplicantHistoryPostRequestDto(

        @NotNull(message = "generation 필수로 입력해주시기 바랍니다.") @Size(min = 1, max = 5, message = "1~5 글자 사이로 입력해주세요.")
        String generation,
        @NotNull(message = "fieldType 필수로 입력해주시기 바랍니다.")
        FieldType fieldType,
        @NotNull(message = "applicationStatus 필수로 입력해주시기 바랍니다.")
        ApplicationStatus applicationStatus,
        @NotNull(message = "memberId 필수로 입력해주시기 바랍니다.")
        Long memberId
) {
}
