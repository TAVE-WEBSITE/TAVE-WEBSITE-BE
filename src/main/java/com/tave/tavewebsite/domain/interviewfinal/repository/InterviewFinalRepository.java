package com.tave.tavewebsite.domain.interviewfinal.repository;

import com.tave.tavewebsite.domain.interviewfinal.entity.InterviewFinal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface InterviewFinalRepository extends JpaRepository<InterviewFinal, Long>, InterviewFinalCustomRepository {

    Page<InterviewFinal> findAllByOrderByInterviewDateAscInterviewTimeAscUsernameAsc(Pageable pageable);

    List<InterviewFinal> findAllByOrderByInterviewDateAscInterviewTimeAscUsernameAsc();

    Optional<InterviewFinal> findByMemberIdAndGeneration(Long memberId, String generation);

    @Query("""
    SELECT i
    FROM InterviewFinal i
    WHERE i.generation = :generation
    ORDER BY 
        i.interviewDate ASC,
        i.interviewTime ASC,
        i.username ASC
    """)
    List<InterviewFinal> findAllByGenerationOrderByInterviewDateTimeUsername(
            @Param("generation") String generation
    );

    List<InterviewFinal> findByInterviewDateAndInterviewTime(LocalDate interviewDate, LocalTime interviewTime);
}
