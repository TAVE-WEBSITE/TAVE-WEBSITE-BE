package com.tave.tavewebsite.domain.resume.dto.request;

public record PortfolioPresignedUrlRequestDto(
        String contentType,
        Long fileSize
) {
}
