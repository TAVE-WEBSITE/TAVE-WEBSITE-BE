package com.tave.tavewebsite.domain.interviewfinal.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record InterviewFinalPageDto(
        int totalPage,
        List<InterviewFinalDetailDto> dataList
) {
    public static InterviewFinalPageDto of(int totalPage, List<InterviewFinalDetailDto> dataList) {
        return InterviewFinalPageDto.builder()
                .totalPage(totalPage)
                .dataList(dataList)
                .build();
    }
}
