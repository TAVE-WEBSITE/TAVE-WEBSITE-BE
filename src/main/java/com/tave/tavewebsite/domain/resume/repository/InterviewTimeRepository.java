package com.tave.tavewebsite.domain.resume.repository;

import com.tave.tavewebsite.domain.resume.entity.InterviewTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface InterviewTimeRepository extends JpaRepository<InterviewTime, Long> {

    Optional<InterviewTime> findByInterviewDetailTime(
            LocalDateTime interviewTime);

}
