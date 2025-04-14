package com.tave.tavewebsite.domain.member.dto.request;

public record ResetPasswordVerifyRequestDto(
        String name,
        String email,
        String birth
) {
}