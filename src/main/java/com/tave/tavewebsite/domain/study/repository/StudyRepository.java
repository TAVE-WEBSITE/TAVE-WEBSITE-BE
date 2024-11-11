package com.tave.tavewebsite.domain.study.repository;

import com.tave.tavewebsite.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
}
