package com.tave.tavewebsite.domain.resume.entity;

import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewTime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String interviewYear;

    private String interviewDate;

    private String interviewTime;

    @Builder
    public InterviewTime(String interviewYear, String interviewDate, String interviewTime) {
        this.interviewYear = interviewYear;
        this.interviewDate = interviewDate;
        this.interviewTime = interviewTime;
    }

    public static InterviewTime of(LocalDateTime localDateTime) {
        return InterviewTime.builder()
                .interviewYear(convertYearToString(localDateTime))
                .interviewDate(convertDateToString(localDateTime))
                .interviewTime(convertTimeToString(localDateTime))
                .build();
    }

    private static String convertYearToString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy"));
    }

    private static String convertDateToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        return localDateTime.format(formatter);
    }

    private static String convertTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(formatter);
    }


}
