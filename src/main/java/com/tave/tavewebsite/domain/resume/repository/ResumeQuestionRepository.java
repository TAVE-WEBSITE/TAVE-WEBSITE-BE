package com.tave.tavewebsite.domain.resume.repository;

import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeQuestion;
import com.tave.tavewebsite.global.common.FieldType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeQuestionRepository extends JpaRepository<ResumeQuestion, Long> {
    List<ResumeQuestion> findByResumeId(Long resumeId);
    List<ResumeQuestion> findByResumeAndFieldType(Resume resume, FieldType fieldType);
    boolean existsByResumeId(Long resumeId);
    void deleteByResumeId(Long resumeId);
}
