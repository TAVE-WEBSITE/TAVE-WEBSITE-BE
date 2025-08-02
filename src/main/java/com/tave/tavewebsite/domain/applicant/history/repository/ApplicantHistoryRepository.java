package com.tave.tavewebsite.domain.applicant.history.repository;

import com.tave.tavewebsite.domain.applicant.history.entity.ApplicantHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantHistoryRepository extends JpaRepository<ApplicantHistory, Long> {
    @Query("SELECT ah FROM ApplicantHistory ah JOIN FETCH ah.member m WHERE m.id = :memberId")
    List<ApplicantHistory> findAllByMemberIdWithMember(@Param("memberId") Long memberId);

    @Modifying
    @Query(value = """
                UPDATE applicant_history ah
                SET application_status = 
                  CASE r.final_document_evaluation_status
                    WHEN 'PASS' THEN 'DOCUMENT_PASSED'
                    WHEN 'FAIL' THEN 'REJECTED'
                    ELSE ah.application_status
                  END
                FROM resume r
                WHERE ah.member_id = r.member_id
                  AND ah.generation = r.resume_generation
            """, nativeQuery = true)
    void bulkSyncApplicationStatusFromResumeDocumentStatus();

    @Modifying
    @Query(value = """
                UPDATE applicant_history ah
                SET application_status = 
                  CASE r.final_document_evaluation_status
                    WHEN 'FINAL_PASS' THEN 'FINAL_ACCEPTED'
                    WHEN 'FINAL_FAIL' THEN 'FINAL_FAIL'
                    WHEN 'FAIL' THEN 'REJECTED'
                    ELSE ah.application_status
                  END
                FROM resume r
                WHERE ah.member_id = r.member_id
                  AND ah.generation = r.resume_generation
            """, nativeQuery = true)
    void bulkSyncApplicationStatusFromResumeInterviewStatus();

}
