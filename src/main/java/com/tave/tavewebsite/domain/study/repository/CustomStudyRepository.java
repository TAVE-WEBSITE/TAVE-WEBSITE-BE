package com.tave.tavewebsite.domain.study.repository;

import com.tave.tavewebsite.domain.study.dto.StudyResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomStudyRepository {

    Page<StudyResponseDto> findStudyByGenerationAndField(String generation, String field, Pageable pageable);

    Page<StudyResponseDto> findStudyByField(String field, Pageable pageable);

    Page<StudyResponseDto> findStudyByGeneration(String generation, Pageable pageable);

    Page<StudyResponseDto> findAllStudy(Pageable pageable);
}
