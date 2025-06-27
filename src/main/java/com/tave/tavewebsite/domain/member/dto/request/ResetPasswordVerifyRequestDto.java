package com.tave.tavewebsite.domain.member.dto.request;
import jakarta.validation.constraints.Email;

public record ResetPasswordVerifyRequestDto(
        String name,

        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,
        String birth,
        boolean reset
) {
}