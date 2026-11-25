package com.tave.tavewebsite.domain.resume.dto.response;

public record PortfolioPresignedUrlResponseDto(
        String presignedUrl,
        String objectKey
) {
}
