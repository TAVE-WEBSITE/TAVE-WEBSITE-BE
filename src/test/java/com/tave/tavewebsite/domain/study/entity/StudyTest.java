package com.tave.tavewebsite.domain.study.entity;

import com.tave.tavewebsite.domain.study.repository.StudyRepository;
import com.tave.tavewebsite.global.common.FieldType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
class StudyTest {

    @Autowired
    private StudyRepository studyRepository;

    @DisplayName("Study 엔티티가 성공적으로 수행되는지 확인합니다.")
    @Test
    void createStudy(){
     //given
        Study study1 = new Study(1L, "teamName1", "topic1", "14", FieldType.BACKEND, "http://blog.url1", "imageUrl");
        Study study2 = new Study(2L, "teamName2", "topic2", "14", FieldType.BACKEND, "http://blog.url2", "imageUrl");

     //when
        studyRepository.save(study1);
        studyRepository.save(study2);

        Study result = studyRepository.findById(1L).get();

     //then
        assertThat(studyRepository.count()).isEqualTo(2);
        assertThat(result)
                .extracting("teamName", "topic", "generation", "blogUrl")
                .contains("teamName1", "topic1", "14", "http://blog.url1");
    }
  
}