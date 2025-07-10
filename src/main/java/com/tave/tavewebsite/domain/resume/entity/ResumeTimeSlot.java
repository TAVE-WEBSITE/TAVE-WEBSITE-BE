package com.tave.tavewebsite.domain.resume.entity;

import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeTimeSlot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(nullable = false)
    private Long interviewTimeId;

    @Column(nullable = false)
    private LocalDateTime interviewDetailTime;

    public static ResumeTimeSlot of(Resume resume, InterviewTime interviewTime) {
        return ResumeTimeSlot.builder().
                resume(resume).
                interviewTimeId(interviewTime.getId()).
                interviewDetailTime(interviewTime.getInterviewDetailTime()).
                build();
    }
}
