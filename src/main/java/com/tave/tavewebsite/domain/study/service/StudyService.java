package com.tave.tavewebsite.domain.study.service;

import com.tave.tavewebsite.domain.study.dto.StudyCreateReq;
import com.tave.tavewebsite.domain.study.dto.StudyModifyDto;
import com.tave.tavewebsite.domain.study.entity.Study;
import com.tave.tavewebsite.domain.study.exception.NotFoundStudy;
import com.tave.tavewebsite.domain.study.repository.StudyRepository;
import com.tave.tavewebsite.global.common.FieldType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyService {

    private final StudyRepository studyRepository;


    @Transactional
    public void createStudy(StudyCreateReq req) {
        // 존재 유무 & 직책에 대한 유저 자격 확인
        // 이미지 업로드하고 req.getImageUrl() 대신 만들어진 String 값을 넣기
        Study study = new Study(req.getTeamName(), req.getTopic(), req.getGeneration(),
                FieldType.valueOf(req.getField()), req.getBlogUrl(), req.getImageUrl());

        studyRepository.save(study);
    }

    public void getStudies(){

    }

    @Transactional
    public void modifyStudy(Long studyId, StudyModifyDto dto){
        // 존재 유무 & 직책에 대한 유저 자격 확인
        Study study = studyRepository.findById(studyId).orElseThrow(NotFoundStudy::new);


    }

    public void deleteStudy(){

    }
}