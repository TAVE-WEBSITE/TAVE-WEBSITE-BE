package com.tave.tavewebsite.domain.project.repository;

import com.tave.tavewebsite.domain.project.dto.response.ProjectResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomProjectRepository {
    Page<ProjectResponseDto> findProjectByGenerationAndField(String generation, String field, Pageable pageable);

    Page<ProjectResponseDto> findProjectByGeneration(String generation, Pageable pageable);

    Page<ProjectResponseDto> findProjectByField(String field, Pageable pageable);

    Page<ProjectResponseDto> findAllProjects(Pageable pageable);
}
