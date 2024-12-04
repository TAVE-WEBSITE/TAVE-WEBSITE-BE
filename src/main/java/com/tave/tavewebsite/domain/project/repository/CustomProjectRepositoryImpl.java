package com.tave.tavewebsite.domain.project.repository;

import com.tave.tavewebsite.domain.project.dto.response.ProjectResponseDto;
import com.tave.tavewebsite.domain.project.entity.Project;
import com.tave.tavewebsite.global.common.FieldType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
public class CustomProjectRepositoryImpl implements CustomProjectRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<ProjectResponseDto> findProjectByGenerationAndField(String generation, String field, Pageable pageable) {
        List<Project> projects = em.createQuery("select p from Project p where p.field=:field and p.generation = :generation", Project.class)
                .setParameter("field", FieldType.valueOf(field))
                .setParameter("generation", generation)
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return getResult(projects, pageable);
    }

    @Override
    public Page<ProjectResponseDto> findProjectByGeneration(String generation, Pageable pageable) {
        List<Project> projects = em.createQuery("select p from Project p where p.generation = :generation", Project.class)
                .setParameter("generation", generation)
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return getResult(projects, pageable);
    }

    @Override
    public Page<ProjectResponseDto> findProjectByField(String field, Pageable pageable) {
        List<Project> projects = em.createQuery("select p from Project p where p.field = :field", Project.class)
                .setParameter("field", FieldType.valueOf(field))
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return getResult(projects, pageable);
    }

    @Override
    public Page<ProjectResponseDto> findAllProjects(Pageable pageable) {
        // 전체 프로젝트를 가져오는 쿼리 수정
        List<Project> projects = em.createQuery("select p from Project p order by p.createdAt", Project.class)
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        log.info("findAllProjects: {}", projects.size());

        return getResult(projects, pageable);
    }

    private Page<ProjectResponseDto> getResult(List<Project> projects, Pageable pageable) {
        List<ProjectResponseDto> projectResponseDtos = projects.stream()
                .map(ProjectResponseDto::new) // Project 객체를 매개변수로 받는 생성자를 사용
                .toList();

        // 페이지네이션을 위한 반환
        return new PageImpl<>(projectResponseDtos, pageable, projects.size());
    }
}

