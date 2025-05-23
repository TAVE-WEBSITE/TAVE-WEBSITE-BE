package com.tave.tavewebsite.domain.interviewplace.repository;

import com.tave.tavewebsite.domain.interviewplace.entity.InterviewPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewPlaceRepository extends JpaRepository<InterviewPlace, Integer> {

    // FIXME 아직 조회 기준을 몰라서 일단 createdAt으로 조회함.
    Optional<InterviewPlace> findFirstByOrderByCreatedAtDesc();

}
