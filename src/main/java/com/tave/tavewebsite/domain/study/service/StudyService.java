package com.tave.tavewebsite.domain.study.service;

import com.tave.tavewebsite.domain.study.dto.StudyRequestDto;
import com.tave.tavewebsite.domain.study.dto.StudyResponseDto;
import com.tave.tavewebsite.domain.study.entity.Study;
import com.tave.tavewebsite.domain.study.exception.NotFoundStudy;
import com.tave.tavewebsite.domain.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    public void createStudy(StudyRequestDto req) {

        Study study = new Study(req);

        studyRepository.save(study);
    }

    @Transactional(readOnly = true)
    public Page<StudyResponseDto> getStudies(String generation, String field, Pageable pageable) {
        Page<StudyResponseDto> map;

        try {
            log.info("field: {}, generation: {}", field, generation);
            if (generation.equals("ALL") && field.equals("ALL"))
                map = studyRepository.findAllStudy(pageable);
            else if (generation.equals("ALL"))
                map = studyRepository.findStudyByField(field, pageable);
            else if (field.equals("ALL"))
                map = studyRepository.findStudyByGeneration(generation, pageable);
            else
                map = studyRepository.findStudyByGenerationAndField(generation, field, pageable);
        } catch (Exception e) {
            throw new NotFoundStudy();
        }

        return map;
    }

    @Transactional
    public void modifyStudy(Long studyId, StudyRequestDto req) {
        // 존재 유무 & 직책에 대한 유저 자격 확인
        Study study = studyRepository.findById(studyId).orElseThrow(NotFoundStudy::new); // 스터디 존재 유무 확인
        study.updateStudy(req);
    }

    public void deleteStudy(Long studyId){
        Study study = studyRepository.findById(studyId).orElseThrow(NotFoundStudy::new); // 스터디 존재 유무 확인
        studyRepository.delete(study);
    }
}