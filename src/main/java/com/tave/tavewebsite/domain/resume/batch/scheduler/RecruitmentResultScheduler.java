package com.tave.tavewebsite.domain.resume.batch.scheduler;

import com.tave.tavewebsite.domain.applicant.history.service.ApplicantHistoryService;
import com.tave.tavewebsite.domain.apply.initial.setup.entity.ApplyInitialSetup;
import com.tave.tavewebsite.domain.apply.initial.setup.repository.ApplyInitialSetupRepository;
import com.tave.tavewebsite.domain.resume.batch.exception.RecruitmentBatchJobException.DocumentResultBatchJobFailException;
import com.tave.tavewebsite.domain.resume.batch.service.RecruitmentEmailBatchService;
import com.tave.tavewebsite.domain.resume.service.ResumeEvaluateService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RecruitmentResultScheduler {

    private final RecruitmentEmailBatchService recruitmentEmailBatchService;
    private final ApplyInitialSetupRepository applyInitialSetupRepository;
    private final ResumeEvaluateService resumeEvaluateService;
    private final ApplicantHistoryService applicantHistoryService;

    @Scheduled(fixedRate = 300_000) // 5분마다 실행
    @SchedulerLock(name = "documentResultEmailJobLock", lockAtMostFor = "PT10M") // 락 10분간 유지
    public void executeDocumentResultIfScheduled() {
        ApplyInitialSetup applyInitialSetup = applyInitialSetupRepository.findById(1L)
                .orElseThrow(DocumentResultBatchJobFailException::new);

        if (applyInitialSetup.getDocumentAnnouncementFlag() == null
                || !applyInitialSetup.getDocumentAnnouncementFlag()) {
            return;
        }

        // 아직 실행되지 않았고, 실행 시점이 지났으면
        if (LocalDateTime.now().isAfter(applyInitialSetup.getDocumentAnnouncementDate())) {
            // 이메일을 보내기 전 사용자 마이 페이지 업데이트
            applicantHistoryService.changeApplicantStatusFromDocumentStatus();
            // 이메일 전송
            recruitmentEmailBatchService.runDocumentResultJobAsync();
            applyInitialSetup.changeDocumentAnnouncementFlag(false);
            applyInitialSetupRepository.save(applyInitialSetup);
        }
    }

    @Scheduled(fixedRate = 300_000) // 5분마다 실행
    @SchedulerLock(name = "lastResultEmailJobLock", lockAtMostFor = "PT10M") // 락 10분간 유지
    public void executeLastResultIfScheduled() {
        ApplyInitialSetup applyInitialSetup = applyInitialSetupRepository.findById(1L)
                .orElseThrow(DocumentResultBatchJobFailException::new);

        if (applyInitialSetup.getLastAnnouncementFlag() == null
                || !applyInitialSetup.getLastAnnouncementFlag()) {
            return;
        }

        // 아직 실행되지 않았고, 실행 시점이 지났으면
        if (LocalDateTime.now().isAfter(applyInitialSetup.getLastAnnouncementDate())) {
            // 이메일 보내기 전 사용자 마이 페이지 업데이트
            applicantHistoryService.changeApplicantStatusFromInterviewStatus();

            // 이메일 전송
            recruitmentEmailBatchService.runLastResultJobAsync();
            applyInitialSetup.changeLastAnnouncementFlag(false);
            applyInitialSetupRepository.save(applyInitialSetup);

            log.info("이력서 전체 삭제가 실행 되었습니다.");
            //이력서 전체 삭제
            resumeEvaluateService.deleteAllResume();
        }
    }
}
