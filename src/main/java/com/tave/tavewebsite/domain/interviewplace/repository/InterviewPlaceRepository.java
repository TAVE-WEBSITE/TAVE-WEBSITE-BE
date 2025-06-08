package com.tave.tavewebsite.domain.interviewplace.repository;

import com.tave.tavewebsite.domain.interviewplace.entity.InterviewPlace;
import com.tave.tavewebsite.global.common.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InterviewPlaceRepository extends JpaRepository<InterviewPlace, Integer> {

    List<InterviewPlace> findByStatus(Status status);

    Optional<InterviewPlace> findByInterviewDayAndStatus(LocalDate interviewDay, Status status);

}
