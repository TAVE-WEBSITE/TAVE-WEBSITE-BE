package com.tave.tavewebsite.domain.study.service;

import com.tave.tavewebsite.domain.study.dto.StudyReq;
import com.tave.tavewebsite.domain.study.dto.StudyResDto;
import com.tave.tavewebsite.domain.study.entity.Study;
import com.tave.tavewebsite.domain.study.exception.NotFoundStudy;
import com.tave.tavewebsite.domain.study.repository.StudyRepository;
import com.tave.tavewebsite.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyService {

    private final StudyRepository studyRepository;
    private final S3Service s3Service;


    @Transactional
    public void createStudy(StudyReq req) {
        // 존재 유무 & 직책에 대한 유저 자격 확인
        // 이미지 업로드하고 req.getImageUrl() 대신 만들어진 String 값을 넣기
        URL url = null;
        if (req.imageFile() != null) {
            url = s3Service.uploadImages(req.imageFile());
        }
        Study study = new Study(req, url);

        studyRepository.save(study);
    }

    public Page<StudyResDto> getStudies(Pageable pageable) {
        Page<Study> studies = studyRepository.findAll(pageable);
        Page<StudyResDto> map = studies.map(study -> new StudyResDto(study.getId(), study.getTeamName(),
                study.getGeneration(), String.valueOf(study.getField()), study.getTopic(),
                study.getImgUrl(), study.getBlogUrl()));

        return map;
    }

    @Transactional
    public void modifyStudy(Long studyId, StudyReq req){
        // 존재 유무 & 직책에 대한 유저 자격 확인
        Study study = studyRepository.findById(studyId).orElseThrow(NotFoundStudy::new); // 스터디 존재 유무 확인
        URL url = s3Service.uploadImages(req.imageFile());
        study.updateStudy(req, url);

        studyRepository.save(study);
    }

    @Transactional
    public void deleteStudy(Long studyId){
        Study study = studyRepository.findById(studyId).orElseThrow(NotFoundStudy::new); // 스터디 존재 유무 확인
        s3Service.deleteImage(study.getImgUrl());
        studyRepository.delete(study);
    }
}