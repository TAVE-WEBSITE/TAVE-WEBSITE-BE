package com.tave.tavewebsite.domain.resume.entity;

import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

    @ManyToOne
    @JoinColumn(name = "interview_time_id", nullable = false)
    private InterviewTime interviewTime;

    public static ResumeTimeSlot of(Resume resume, InterviewTime interviewTime) {
        return ResumeTimeSlot.builder().
                resume(resume).
                interviewTime(interviewTime).
                build();
    }
}
