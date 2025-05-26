package com.tave.tavewebsite.domain.interviewfinal.repository;

import com.tave.tavewebsite.domain.interviewfinal.entity.InterviewFinal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewFinalRepository extends JpaRepository<InterviewFinal, Integer> {

    Page<InterviewFinal> findAllByOrderByInterviewDayAscInterviewTimeAscUsernameAsc(Pageable pageable);

    List<InterviewFinal> findAllByOrderByInterviewDayAscInterviewTimeAscUsernameAsc();

}
