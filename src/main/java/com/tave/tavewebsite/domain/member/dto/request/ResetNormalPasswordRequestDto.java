package com.tave.tavewebsite.domain.member.dto.request;

import jakarta.validation.constraints.Pattern;

public record ResetNormalPasswordRequestDto(
        String email,
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).+$",
                message = "비밀번호는 8자 이상이어야 하며, 대문자, 소문자, 특수문자를 포함해야 합니다.")
        String password,
        String validatedPassword
) {
}
