package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotReqDto;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeState;
import com.tave.tavewebsite.domain.resume.entity.ResumeTimeSlot;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.domain.resume.repository.ResumeTimeSlotRepository;
import com.tave.tavewebsite.global.common.FieldType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResumeResumeTimeServiceImplTest {

    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private ResumeTimeSlotRepository resumeTimeSlotRepository;

    @InjectMocks
    private ResumeTimeService resumeTimeService;

    @DisplayName("체크한 면접 가능 시간을 DB에 저장합니다.")
    @Test
    void createTimeSlot() {
        // Given
        // Resume 도메인 객체를 생성(빌더를 사용하거나, 간단히 new Resume() 후 id를 설정)
        Member member = mock(Member.class);

        // 실제 Resume 객체를 생성 (빌더 패턴 사용)
        Resume resumeReal = Resume.builder()
                .school("세종대")
                .major("경영학과")
                .resumeGeneration(15)
                .blogUrl("www.blog.com")
                .githubUrl("www.github.com")
                .state(ResumeState.TEMPORARY)
                .field(FieldType.BACKEND)
                .member(member)
                .build();

        // spy를 사용하여 실제 객체의 동작을 유지하면서 getId()만 오버라이드
        Resume resume = spy(resumeReal);
        ReflectionTestUtils.setField(resume, "id", 1L);

        when(resumeRepository.findById(resume.getId())).thenReturn(Optional.of(resume));

        List<TimeSlotReqDto> reqDtos = new ArrayList<>();
        reqDtos.add(new TimeSlotReqDto(LocalDateTime.now()));

        // When
        resumeTimeService.createTimeSlot(1L, reqDtos);

        // Then
        verify(resumeTimeSlotRepository, times(reqDtos.size())).save(any(ResumeTimeSlot.class));
    }

}