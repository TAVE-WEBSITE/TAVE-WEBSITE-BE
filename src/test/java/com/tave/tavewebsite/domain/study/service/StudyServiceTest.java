package com.tave.tavewebsite.domain.study.service;

import com.tave.tavewebsite.domain.study.entity.Study;
import com.tave.tavewebsite.domain.study.repository.StudyRepository;
import com.tave.tavewebsite.global.common.FieldType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(profiles = "test")
@SpringBootTest
class StudyServiceTest {

    @Autowired
    private StudyService studyService;

    @Autowired
    private StudyRepository studyRepository;

    @DisplayName("스터디가 성공적으로 생성된다.")
    @Test
    void createStudy(){
     //given

     //when

     //then
    }
}