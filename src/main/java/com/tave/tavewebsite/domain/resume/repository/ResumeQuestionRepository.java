package com.tave.tavewebsite.domain.resume.repository;

import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeQuestion;
import com.tave.tavewebsite.global.common.FieldType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ResumeQuestionRepository extends JpaRepository<ResumeQuestion, Long> {
    List<ResumeQuestion> findByResumeId(Long resumeId);

    // todo N+1 문제 해결을 위해서 Fetch Join!
    List<ResumeQuestion> findByResumeAndFieldType(Resume resume, FieldType fieldType);
}
