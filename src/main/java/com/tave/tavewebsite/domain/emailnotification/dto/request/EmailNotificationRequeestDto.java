package com.tave.tavewebsite.domain.emailnotification.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmailNotificationRequeestDto(
        @NotNull(message = "50자 이하로 입력해주세요.") @Size(min = 1, max = 50, message = "1~5 글자 사이로 입력해주세요.")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "유효한 이메일 주소를 입력하세요.")
        String email
) {
}