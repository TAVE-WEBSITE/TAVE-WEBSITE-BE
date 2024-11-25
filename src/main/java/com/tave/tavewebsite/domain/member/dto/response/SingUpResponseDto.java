package com.tave.tavewebsite.domain.member.dto.response;

import com.tave.tavewebsite.global.security.entity.JwtToken;

public record SingUpResponseDto(
        JwtToken jwtToken
) {
}
