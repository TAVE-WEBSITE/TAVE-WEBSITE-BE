package com.tave.tavewebsite.domain.interviewfinal.dto;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.Builder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

@Builder
public record InterviewFormInputStreamDto(
        InputStreamResource inputStreamResource,
        HttpHeaders headers,
        long contentLength
) {
    public static InterviewFormInputStreamDto from(S3ObjectInputStream s3ObjectInputStream, HttpHeaders headers, long contentLength) throws IOException {
        return InterviewFormInputStreamDto.builder()
                .inputStreamResource(new InputStreamResource(s3ObjectInputStream))
                .headers(headers)
                .contentLength(contentLength)
                .build();
    }
}
