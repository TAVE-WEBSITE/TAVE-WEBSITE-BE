package com.tave.tavewebsite.domain.resume.repository;

import com.tave.tavewebsite.domain.resume.entity.InterviewTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewTimeRepository extends JpaRepository<InterviewTime, Long> {

    Optional<InterviewTime> findByInterviewDateAndInterviewTime(String interviewDate, String interviewTime);

}
