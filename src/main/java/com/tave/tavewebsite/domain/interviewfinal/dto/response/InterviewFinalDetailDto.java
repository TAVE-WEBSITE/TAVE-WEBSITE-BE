package com.tave.tavewebsite.domain.interviewfinal.dto.response;

import com.tave.tavewebsite.domain.interviewfinal.entity.InterviewFinal;
import lombok.Builder;


@Builder
public record InterviewFinalDetailDto(
        Long id,
        String field,
        String username,
        String sex,
        String university,
        String interviewDate,
        Long memberId,
        Long resumeId
) {
    public static InterviewFinalDetailDto from(InterviewFinal interviewFinal) {

        String interviewDate = interviewFinal.getInterviewDay() + " " + interviewFinal.getInterviewTime();

        return InterviewFinalDetailDto.builder()
                .id(interviewFinal.getId())
                .field(interviewFinal.getFieldType().getDisplayName())
                .username(interviewFinal.getUsername())
                .sex(interviewFinal.getSex().getDisplayName())
                .university(interviewFinal.getUniversity())
                .interviewDate(interviewDate)
                .memberId(interviewFinal.getMemberId())
                .resumeId(interviewFinal.getResumeId())
                .build();
    }
}
