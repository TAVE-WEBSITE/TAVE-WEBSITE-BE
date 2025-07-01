package com.tave.tavewebsite.domain.applicant.history.service;

import com.tave.tavewebsite.domain.applicant.history.dto.response.ApplicantHistoryResponseDto;
import com.tave.tavewebsite.domain.applicant.history.entity.ApplicantHistory;
import com.tave.tavewebsite.domain.applicant.history.repository.ApplicantHistoryRepository;
import com.tave.tavewebsite.domain.applicant.history.util.ApplicantHistoryMapper;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ApplicantHistoryService {

    private final ApplicantHistoryRepository applicantHistoryRepository;
    private final RedisUtil redisUtil;

    @Transactional(readOnly = true)
    public List<ApplicantHistoryResponseDto> getApplicantHistory(Long memberId) {
        List<ApplicantHistory> histories = applicantHistoryRepository.findAllByMemberIdWithMember(memberId);
        return histories.stream()
                .map(ApplicantHistoryMapper::toResponseDto)
                .toList();
    }

    public void changeApplicantStatusFromDocumentStatus() {
        log.info("현재 완료된 서류 평가 status를 기준으로 벌크 업데이트를 진행합니다.");
        applicantHistoryRepository.bulkSyncApplicationStatusFromResumeDocumentStatus();
    }

    public void changeApplicantStatusFromInterviewStatus(HttpServletRequest request) {
        log.info("현재 완료된 면접 평가 status를 기준으로 벌크 업데이트를 진행합니다.");
        applicantHistoryRepository.bulkSyncApplicationStatusFromResumeInterviewStatus();
    }

}
