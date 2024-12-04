package com.tave.tavewebsite.domain.member.dto.response;

import com.tave.tavewebsite.global.security.entity.JwtToken;

public record RefreshResponseDto(
        String grantType,
        String accessToken
) {
    public static RefreshResponseDto from(JwtToken jwtToken) {
        return new RefreshResponseDto(jwtToken.getGrantType(), jwtToken.getAccessToken());
    }
}
