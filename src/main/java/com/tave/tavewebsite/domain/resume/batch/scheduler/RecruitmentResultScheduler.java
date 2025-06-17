package com.tave.tavewebsite.domain.resume.batch.scheduler;

import com.tave.tavewebsite.domain.apply.initial.setup.entity.ApplyInitialSetup;
import com.tave.tavewebsite.domain.apply.initial.setup.repository.ApplyInitialSetupRepository;
import com.tave.tavewebsite.domain.resume.batch.exception.RecruitmentBatchJobException.DocumentResultBatchJobFailException;
import com.tave.tavewebsite.domain.resume.batch.service.RecruitmentEmailBatchService;
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

    @Scheduled(fixedRate = 300_000) // 5분마다 실행
    @SchedulerLock(name = "documentResultEmailJobLock", lockAtMostFor = "PT10M") // 락 10분간 유지
    public void executeIfScheduled() {
        ApplyInitialSetup applyInitialSetup = applyInitialSetupRepository.findById(1L)
                .orElseThrow(DocumentResultBatchJobFailException::new);

        if (applyInitialSetup.getDocumentAnnouncementFlag() == null
                || !applyInitialSetup.getDocumentAnnouncementFlag()) {
            return;
        }

        // 아직 실행되지 않았고, 실행 시점이 지났으면
        if (LocalDateTime.now().isAfter(applyInitialSetup.getDocumentAnnouncementDate())) {
            recruitmentEmailBatchService.runDocumentResultJobAsync();
            applyInitialSetup.changeDocumentAnnouncementFlag(false);
            applyInitialSetupRepository.save(applyInitialSetup);
        }
    }
}
