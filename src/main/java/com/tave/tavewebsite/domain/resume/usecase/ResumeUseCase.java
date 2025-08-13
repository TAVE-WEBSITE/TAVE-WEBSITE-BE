package com.tave.tavewebsite.domain.resume.usecase;

import com.tave.tavewebsite.domain.interviewfinal.dto.S3FileInputStreamDto;
import com.tave.tavewebsite.domain.interviewfinal.entity.InterviewFinal;
import com.tave.tavewebsite.domain.interviewfinal.service.InterviewGetService;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeListResponse;
import com.tave.tavewebsite.domain.resume.dto.response.ResumePortfolioDto;
import com.tave.tavewebsite.domain.resume.service.ResumeGetService;
import com.tave.tavewebsite.domain.resume.service.ResumeQuestionService;
import com.tave.tavewebsite.global.s3.service.S3DownloadSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeUseCase {

    private final ResumeQuestionService resumeQuestionService;
    private final InterviewGetService interviewGetService;
    private final ResumeGetService resumeGetService;
    private final S3DownloadSerivce s3DownloadSerivce;

    public ResumeListResponse getResumeByInterviewDateTime(LocalDate date, LocalTime time) {
        // 면접 날짜 + 시간에 있는 지원자의 ResumeIdList 가져오기
        List<Long> resumeIdList = interviewGetService.getInterviewFinalByDateAndTime(date, time)
                .stream()
                .map(InterviewFinal::getResumeId)
                .toList();

        return resumeQuestionService.getResumeListDetails(resumeIdList);
    }

    public S3FileInputStreamDto downloadPortfolio(Long resumeId) throws IOException {
        ResumePortfolioDto dto = resumeGetService.getResumeWithMemberByResumeId(resumeId);
        return s3DownloadSerivce.downloadPortfolioPDF(dto.portfolioUrl(), dto.memberName());
    }

}
