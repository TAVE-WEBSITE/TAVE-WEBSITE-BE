package com.tave.tavewebsite.domain.interviewfinal.dto;

import com.tave.tavewebsite.domain.member.entity.Sex;
import com.tave.tavewebsite.global.common.FieldType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;


@Builder
public record InterviewFinalConvertDto(
        String username,
        String email,
        Integer generation,
        Sex sex,
        FieldType fieldType,
        String university,
        LocalDate interviewDate,
        LocalTime interviewTime
) {

    public static InterviewFinalConvertDto from(String username, String email, Integer generation, Sex sex, FieldType fieldType, String university, LocalDate interviewDate, LocalTime interviewTime) {
        return InterviewFinalConvertDto.builder()
                .username(username)
                .email(email)
                .generation(generation)
                .sex(sex)
                .fieldType(fieldType)
                .university(university)
                .interviewDate(interviewDate)
                .interviewTime(interviewTime)
                .build();
    }

}
