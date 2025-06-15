package com.tave.tavewebsite.domain.interviewfinal.dto.response;

import com.tave.tavewebsite.domain.interviewplace.dto.response.InterviewPlaceDetailDto;
import lombok.Builder;

@Builder
public record InterviewFinalForMemberDto(
        String interviewDate,
        String interviewTime,
        String dayName,
        String generalAddress,
        String detailAddress,
        String openChatLink,
        String code
) {
    public static InterviewFinalForMemberDto of(String interviewDate, String interviewTime, String dayName, InterviewPlaceDetailDto detailDto) {

        return InterviewFinalForMemberDto.builder()
                .interviewDate(interviewDate)
                .interviewTime(interviewTime)
                .dayName(dayName)
                .generalAddress(detailDto.generalAddress())
                .detailAddress(detailDto.detailAddress())
                .openChatLink(detailDto.openChatLink())
                .code(detailDto.code())
                .build();
    }
}
