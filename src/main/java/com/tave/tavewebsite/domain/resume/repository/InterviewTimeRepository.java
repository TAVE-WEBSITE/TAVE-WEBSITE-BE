package com.tave.tavewebsite.domain.resume.repository;

import com.tave.tavewebsite.domain.resume.entity.InterviewTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InterviewTimeRepository extends JpaRepository<InterviewTime, Long> {

    Optional<InterviewTime> findByInterviewDetailTime(
            LocalDateTime interviewTime);

    @Query("SELECT DISTINCT CAST(i.interviewDetailTime AS localdate) " +
            "FROM InterviewTime i " +
            "ORDER BY CAST(i.interviewDetailTime AS localdate)")
    List<LocalDate> findDistinctInterviewDates();
}
