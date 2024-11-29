package com.tave.tavewebsite.domain.study.service;

import com.tave.tavewebsite.domain.study.dto.StudyRequestDto;
import com.tave.tavewebsite.domain.study.entity.Study;
import com.tave.tavewebsite.domain.study.repository.StudyRepository;
import com.tave.tavewebsite.global.s3.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class StudyServiceTest {

    @InjectMocks
    private StudyService studyService;

    @Mock
    private StudyRepository studyRepository;

    @Mock
    private S3Service s3Service;

    @Captor
    private ArgumentCaptor<Study> studyCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("스터디가 성공적으로 생성된다.")
    @Test
    void createStudyTest() throws Exception {
        // Given
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test-image.jpg", "image/jpeg", "image content".getBytes()
        );
        URL mockUrl = new URL("https://example.com/test-image.jpg");

        StudyRequestDto req = new StudyRequestDto(
                "TeamName",
                "14",
                "BACKEND",
                "topic",
                "https://example.com/blog"
        );

        when(s3Service.uploadImages(mockFile)).thenReturn(mockUrl);

        // When
        studyService.createStudy(req, mockFile);

        // Then
        verify(studyRepository, times(1)).save(studyCaptor.capture());
        Study savedStudy = studyCaptor.getValue();

        assertThat(savedStudy.getTeamName()).isEqualTo(req.teamName());
    }
}