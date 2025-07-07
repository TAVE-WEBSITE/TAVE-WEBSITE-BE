package com.tave.tavewebsite.domain.resume.repository;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>, ResumeCustomRepository {

    Optional<Resume> findByMember(Member member);

    @Query("select r from Resume r join fetch r.languageLevels join fetch r.member where r.id = :id")
    Resume findResumeWithLanguageLevels(@Param("id") Long id);

    Optional<Resume> findByMemberId(Long memberId);

    long count();

    long countByFinalDocumentEvaluationStatus(EvaluationStatus status);

    @Query("SELECT MIN(r.id) FROM Resume r")
    Long findMinId();

    @Query("SELECT MAX(r.id) FROM Resume r")
    Long findMaxId();

    @EntityGraph(attributePaths = {
            "resumeQuestions",
            "interviewTimes",
            "programingLanguages"
    })
    Optional<Resume> findWithAllRelationsById(Long id);

    @Query("SELECT r as resume, rs as resumeTimeSlot, m as member " +
            "FROM Resume r " +
            "LEFT JOIN ResumeTimeSlot rs ON r.id = rs.resume.id " +
            "LEFT JOIN Member m ON r.member.id = m.id " +
            "WHERE r.resumeGeneration = :generation AND r.finalDocumentEvaluationStatus = :evaluationStatus")
    List<Tuple> findResumesWithInterviewTimesAndMemberByGenerationAndStatus(
            @Param("generation") String generation,
            @Param("evaluationStatus") EvaluationStatus evaluationStatus);

    // FIXME - BETA TEST REPO 기능
    @Query("""
            SELECT r
            FROM Resume r
            JOIN FETCH r.member m
            LEFT JOIN FETCH r.resumeTimeSlots rts
            WHERE r.finalDocumentEvaluationStatus = :status
            """)
    List<Resume> findAllWithMemberAndTimeSlotsByStatus(
            @Param("status") EvaluationStatus status
    );

    @Query("""
            SELECT DISTINCT r
            FROM Resume r
            LEFT JOIN FETCH r.resumeTimeSlots
            WHERE r.id IN :resumeIds
            """)
    List<Resume> findAllWithResumeTimeSlotsByIdIn(@Param("resumeIds") List<Long> resumeIds);

}
