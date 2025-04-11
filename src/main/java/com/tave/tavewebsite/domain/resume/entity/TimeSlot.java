package com.tave.tavewebsite.domain.resume.entity;

import com.tave.tavewebsite.domain.resume.exception.NotValidTimeException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Builder
    public TimeSlot(LocalDateTime time, Resume resume) {
        this.time = time;
        this.resume = resume;
        resume.getTimeSlots().add(this);
    }

    public static TimeSlot of(LocalDateTime time, Resume resume) {
        return TimeSlot.builder().
                time(time).
                resume(resume).
                build();
    }
}
