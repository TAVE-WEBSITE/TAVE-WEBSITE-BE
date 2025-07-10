package com.tave.tavewebsite.domain.resume.entity;

import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewTime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime interviewDetailTime;

    @Builder
    public InterviewTime(LocalDateTime interviewTime) {
        this.interviewDetailTime = interviewTime;
    }

    public static InterviewTime of(LocalDateTime localDateTime) {
        return InterviewTime.builder()
                .interviewTime(localDateTime)
                .build();
    }

    public LocalDateTime getTime() {
        return interviewDetailTime;
    }

}
