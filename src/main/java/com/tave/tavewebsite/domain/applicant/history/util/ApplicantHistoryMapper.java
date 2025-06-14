package com.tave.tavewebsite.domain.applicant.history.util;

import com.tave.tavewebsite.domain.applicant.history.dto.request.ApplicantHistoryPostRequestDto;
import com.tave.tavewebsite.domain.applicant.history.dto.response.ApplicantHistoryResponseDto;
import com.tave.tavewebsite.domain.applicant.history.entity.ApplicantHistory;
import com.tave.tavewebsite.domain.member.entity.Member;

public class ApplicantHistoryMapper {

    public static ApplicantHistory postDtoToApplicantHistory(ApplicantHistoryPostRequestDto requestDto, Member member) {
        return new ApplicantHistory(
                requestDto.generation(),
                requestDto.fieldType(),
                requestDto.applicationStatus(),
                member);
    }

    public static ApplicantHistoryResponseDto toResponseDto(ApplicantHistory entity) {
        return new ApplicantHistoryResponseDto(
                entity.getGeneration(),
                entity.getFieldType().name(),
                entity.getApplicationStatus().name()
        );
    }
}
