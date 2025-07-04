package com.tave.tavewebsite.domain.interviewfinal.dto;

import com.tave.tavewebsite.domain.member.dto.response.MemberResumeDto;
import com.tave.tavewebsite.domain.member.entity.Sex;
import com.tave.tavewebsite.global.common.FieldType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record InterviewFinalSaveDto(
        String username,
        String email,
        String generation,
        Sex sex,
        FieldType fieldType,
        String university,
        LocalDate interviewDate,
        LocalTime interviewTime,
        Long memberId,
        Long resumeId
) {
    public static InterviewFinalSaveDto analysisSucceeded(InterviewFinalConvertDto interviewDto, MemberResumeDto memberResumeDto) {
        return InterviewFinalSaveDto.builder()
                .username(interviewDto.username())
                .email(interviewDto.email())
                .generation(interviewDto.generation())
                .sex(interviewDto.sex())
                .fieldType(interviewDto.fieldType())
                .university(interviewDto.university())
                .interviewDate(interviewDto.interviewDate())
                .interviewTime(interviewDto.interviewTime())
                .memberId(memberResumeDto.memberId())
                .resumeId(memberResumeDto.resumeId())
                .build();
    }

    public static InterviewFinalSaveDto analysisFailed(InterviewFinalConvertDto dto) {
        /*
        * Q email 조회 실패 시 interviewDay와 interviewTime도 "분석 실패"로 저장할 지..?
        * 현재는 2안
        *
        * ex. email: 잘못된 이메일, interviewDay: 10월 10일, interviewTime: 13:00
        * 위와 같은 경우
        * 1안 - DB에 email: "분석 실패", interviewDay: "분석 실패" , interviewTime: "분석 실패"
        * or
        * 2안 - DB에 email: "분석 실패", interviewDay: "10월 10일" , interviewTime: "13:00"
        * */
        return InterviewFinalSaveDto.builder()
                .username(dto.username())
                .email(dto.email())
                .generation(dto.generation())
                .sex(dto.sex())
                .fieldType(dto.fieldType())
                .university(dto.university())
                .interviewDate(dto.interviewDate())
                .interviewTime(dto.interviewTime())
                .memberId(null)
                .resumeId(null)
                .build();
    }




}
