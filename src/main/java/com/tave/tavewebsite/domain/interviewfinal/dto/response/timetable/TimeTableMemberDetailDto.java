package com.tave.tavewebsite.domain.interviewfinal.dto.response.timetable;

import com.tave.tavewebsite.domain.interviewfinal.entity.InterviewFinal;
import lombok.Builder;

@Builder
public record TimeTableMemberDetailDto(
        Long interviewFinalId,
        String field,
        String username,
        Long memberId,
        Long resumeId
) {
    public static TimeTableMemberDetailDto from(InterviewFinal interviewFinal) {
        return TimeTableMemberDetailDto.builder()
                .interviewFinalId(interviewFinal.getId())
                .field(interviewFinal.getFieldType().getDisplayName())
                .username(interviewFinal.getUsername())
                .memberId(interviewFinal.getMemberId())
                .resumeId(interviewFinal.getResumeId())
                .build();
    }
}
