package com.tave.tavewebsite.domain.interviewfinal.dto.response.timetable;

import com.tave.tavewebsite.domain.interviewfinal.entity.InterviewFinal;
import lombok.Builder;

@Builder
public record TimeTableMemberDetailDto(
        Long interviewFinalId,
        String field,
        String username,
        String interviewDate,
        String interviewTime,
        Long memberId,
        Long resumeId
) {
    public static TimeTableMemberDetailDto from(InterviewFinal interviewFinal) {
        return TimeTableMemberDetailDto.builder()
                .interviewFinalId(interviewFinal.getId())
                .field(interviewFinal.getFieldType().getDisplayName())
                .username(interviewFinal.getUsername())
                .interviewDate(interviewFinal.getInterviewDate().toString())
                .interviewTime(interviewFinal.getInterviewTime().toString())
                .memberId(interviewFinal.getMemberId())
                .resumeId(interviewFinal.getResumeId())
                .build();
    }
}
