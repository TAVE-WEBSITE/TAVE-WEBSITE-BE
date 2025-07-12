package com.tave.tavewebsite.domain.resume.usecase;

import com.tave.tavewebsite.domain.interviewfinal.entity.InterviewFinal;
import com.tave.tavewebsite.domain.interviewfinal.service.InterviewGetService;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeListResponse;
import com.tave.tavewebsite.domain.resume.service.ResumeQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeUseCase {

    private final ResumeQuestionService resumeQuestionService;
    private final InterviewGetService interviewGetService;

    public ResumeListResponse getResumeByInterviewDateTime(LocalDate date, LocalTime time) {
        // 면접 날짜 + 시간에 있는 지원자의 ResumeIdList 가져오기
        List<Long> resumeIdList = interviewGetService.getInterviewFinalByDateAndTime(date, time)
                .stream()
                .map(InterviewFinal::getResumeId)
                .toList();

        return resumeQuestionService.getResumeListDetails(resumeIdList);
    }
}
