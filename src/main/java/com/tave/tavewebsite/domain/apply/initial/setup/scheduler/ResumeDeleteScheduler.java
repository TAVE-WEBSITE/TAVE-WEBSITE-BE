package com.tave.tavewebsite.domain.apply.initial.setup.scheduler;

import com.tave.tavewebsite.domain.apply.initial.setup.dto.response.ApplyInitialSetupReadResponseDto;
import com.tave.tavewebsite.domain.apply.initial.setup.entity.ApplyInitialSetup;
import com.tave.tavewebsite.domain.apply.initial.setup.service.ApplyInitialSetupService;
import com.tave.tavewebsite.domain.resume.batch.exception.RecruitmentBatchJobException;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.domain.resume.service.ResumeEvaluateService;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResumeDeleteScheduler {

    private final ApplyInitialSetupService applyInitialSetupService;
    private final RedisUtil redisUtil;
    private final ResumeEvaluateService resumeEvaluateService;

    @Scheduled(fixedRate = 300_000) // 5분마다 실행
    @SchedulerLock(name = "deleteAllResumeJobLock", lockAtMostFor = "PT10M") // 락 10분간 유지
    public void executeDocumentResultIfScheduled() {
        ApplyInitialSetupReadResponseDto initialSetup = applyInitialSetupService.getInitialSetup();

        if (!redisUtil.hasKey("delete_all_resume")){
            return;
        }


        // 삭제 예약이 됐고, 최종 발표 시점이 지났으면
        if (LocalDateTime.now().isAfter(initialSetup.lastAnnouncementDate())) {
            log.info("이력서 전체 삭제가 실행 되었습니다.");
            //이력서 전체 삭제
            resumeEvaluateService.deleteAllResume();
            // 중복 실행 방지
            redisUtil.delete("delete_all_resume");
        }
    }
}
