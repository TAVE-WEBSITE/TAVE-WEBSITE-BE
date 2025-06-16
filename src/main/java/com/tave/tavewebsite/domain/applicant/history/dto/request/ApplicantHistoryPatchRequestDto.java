package com.tave.tavewebsite.domain.applicant.history.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ApplicantHistoryPatchRequestDto(
        @NotNull(message = "필수로 입력하셔야합니다.") @Size(min = 1, max = 5, message = "1~5 글자 사이로 입력해주세요.")
        @Pattern(regexp = "^[0-9]+$", message = "숫자만 입력 가능합니다.")
        String generation
) {
}
