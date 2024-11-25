package com.tave.tavewebsite.domain.member.dto.request;

public record SignUpRequestDto(
        String email,
        String password
) {
}
