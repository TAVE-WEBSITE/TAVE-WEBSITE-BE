package com.tave.tavewebsite.domain.study.repository;

import com.tave.tavewebsite.domain.study.dto.StudyResponseDto;
import com.tave.tavewebsite.domain.study.entity.Study;
import com.tave.tavewebsite.global.common.FieldType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
public class CustomStudyRepositoryImpl implements CustomStudyRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<StudyResponseDto> findStudyByGenerationAndField(String generation, String field, Pageable pageable) {

        List<Study> studies = em.createQuery("select s from Study s where s.field=:field and s.generation = :generation", Study.class)
                .setParameter("field", FieldType.valueOf(field))
                .setParameter("generation", generation)
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return getResult(studies, pageable);
    }

    @Override
    public Page<StudyResponseDto> findStudyByField(String field, Pageable pageable) {
        List<Study> studies = em.createQuery("select s from Study s where s.field = :field", Study.class)
                .setParameter("field", FieldType.valueOf(field))
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return getResult(studies, pageable);
    }

    @Override
    public Page<StudyResponseDto> findStudyByGeneration(String generation, Pageable pageable) {
        List<Study> studies = em.createQuery("select s from Study s where s.generation = :generation", Study.class)
                .setParameter("generation", generation)
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return getResult(studies, pageable);
    }

    @Override
    public Page<StudyResponseDto> findAllStudy(Pageable pageable) {
        List<Study> studies = em.createQuery("select s from Study s order by s.createdAt", Study.class)
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        log.info("findAllStudy: {}", studies.size());

        return getResult(studies, pageable);
    }

    private Page<StudyResponseDto> getResult(List<Study> studies, Pageable pageable) {
        List<StudyResponseDto> studyResponseDtos = studies.stream().map(study -> new StudyResponseDto(study.getId(), study.getTeamName(),
                study.getGeneration(), String.valueOf(study.getField()), study.getTopic(),
                study.getImgUrl(), study.getBlogUrl())).toList();

        return new PageImpl<>(studyResponseDtos, pageable, studies.size());
    }


}
