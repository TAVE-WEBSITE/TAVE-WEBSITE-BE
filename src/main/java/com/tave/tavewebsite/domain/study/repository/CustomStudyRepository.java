package com.tave.tavewebsite.domain.study.repository;

import com.tave.tavewebsite.domain.study.dto.StudyResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomStudyRepository {

    Page<StudyResDto> findStudyByGenerationAndField(String generation, String field, Pageable pageable);

    Page<StudyResDto> findStudyByField(String field, Pageable pageable);

    Page<StudyResDto> findStudyByGeneration(String generation, Pageable pageable);

    Page<StudyResDto> findAllStudy(Pageable pageable);
}
