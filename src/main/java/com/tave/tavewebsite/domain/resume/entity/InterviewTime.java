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

    @Column(nullable = false)
    private Long resumeId;

    @Builder
    public InterviewTime(LocalDateTime interviewTime, Long resumeId) {
        this.interviewDetailTime = interviewTime;
        this.resumeId = resumeId;
    }

    public static InterviewTime of(LocalDateTime localDateTime, Long resumeId) {
        return InterviewTime.builder()
                .interviewTime(localDateTime)
                .resumeId(resumeId)
                .build();
    }

    public LocalDateTime getTime() {
        return interviewDetailTime;
    }

}
