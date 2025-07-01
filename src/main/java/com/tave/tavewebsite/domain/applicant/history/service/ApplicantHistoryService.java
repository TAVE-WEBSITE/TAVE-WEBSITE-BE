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
        log.info("모든 이력서 최종 평가 완료, 실행자 ip : {}, 기기 종류 : {}", request.getRemoteAddr(),
                request.getHeader("User-Agent"));

        String key = "delete_all_resume";

        // redis에 모든 이력서 삭제 키 설정
        redisUtil.set(key, "SCHEDULED", 30 * 24 * 60); //초기 설정의 최종 발표날이 지나면 이 키를 확인한 후 이력서 삭제를 진행한다.
        applicantHistoryRepository.bulkSyncApplicationStatusFromResumeInterviewStatus();
    }

}
