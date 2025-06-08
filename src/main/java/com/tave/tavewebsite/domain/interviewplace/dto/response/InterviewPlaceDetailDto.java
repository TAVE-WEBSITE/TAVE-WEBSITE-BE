package com.tave.tavewebsite.domain.interviewplace.dto.response;

import com.tave.tavewebsite.domain.interviewplace.entity.InterviewPlace;
import lombok.Builder;

@Builder
public record InterviewPlaceDetailDto(
        Long id,
        String interviewDay,
        String generalAddress,
        String detailAddress,
        String openChatLink,
        String code
) {

    public static InterviewPlaceDetailDto of(InterviewPlace interviewPlace) {
        return InterviewPlaceDetailDto.builder()
                .id(interviewPlace.getId())
                .interviewDay(interviewPlace.getInterviewDay().toString())
                .generalAddress(interviewPlace.getGeneralAddress())
                .detailAddress(interviewPlace.getDetailAddress())
                .openChatLink(interviewPlace.getOpenChatLink())
                .code(interviewPlace.getCode())
                .build();
    }
}
