package com.tave.tavewebsite.domain.programinglaunguage.repository;

import com.tave.tavewebsite.domain.programinglaunguage.entity.LanguageLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageLevelRepository extends JpaRepository<LanguageLevel, Long> {
    void deleteByResumeId(Long resumeId);
}
