package com.tave.tavewebsite.domain.apply.dashboard.service;

import com.tave.tavewebsite.domain.apply.dashboard.dto.DashboardRatioResDto;
import com.tave.tavewebsite.domain.apply.dashboard.dto.DashboardResDto;
import com.tave.tavewebsite.domain.apply.dashboard.entity.Dashboard;
import com.tave.tavewebsite.domain.apply.dashboard.exception.DashboardErrorException;
import com.tave.tavewebsite.domain.apply.dashboard.repository.DashboardRepository;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final RedisUtil redisUtil;
    private final RedissonClient redissonClient;

    public DashboardResDto getDashboard() {
        Dashboard dashboard = findIfExists();
        double generationRatio = dashboard.getTotalCount() == 0 ? 0.0 :
                (double) (dashboard.getTotalCount() - dashboard.getPreviousCount()) / dashboard.getTotalCount() * 100;
        long tempCount = redisUtil.countResumeKeysWithPrefix();

        Long totalCount = dashboard.getTotalCount();
        List<DashboardRatioResDto> dashboardRatioResDtosBySex = extractedSexDto(totalCount, dashboard);
        List<DashboardRatioResDto> dashboardRatioResDtosByField = extractedFieldDto(totalCount, dashboard);

        return new DashboardResDto(totalCount, generationRatio, tempCount,
                dashboardRatioResDtosBySex, dashboardRatioResDtosByField);
    }

    // 지원 초기 설정 시 대시보드도 초기화
    @Transactional
    public void initDashboard() {
        if(dashboardRepository.existsById(1L)){
            dashboardRepository.findById(1L).ifPresent(Dashboard::initDashboard);
            return;
        }
        Dashboard dashboard = new Dashboard(0L);
        dashboardRepository.save(dashboard);
    }

    public void updateDashboardAtomically(Resume resume, Member member) {
        Dashboard dashboard = findIfExists();
        dashboard.updateDashboard(resume, member);
    }

    public void addDetailedCount(Resume resume, Member member) {
        String lockKey = "sync:dashboard:lock";
        RLock lock = redissonClient.getLock(lockKey);

        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(10, 2, TimeUnit.SECONDS);
            if(!isLocked){
                log.warn("락을 획득하지 못했습니다.");
            }
            // 로직 처리
            updateDashboardAtomically(resume, member);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private Dashboard findIfExists() {
        return dashboardRepository.findById(1L).orElseThrow(DashboardErrorException::new);
    }

    private double getRatio(Long totalCount, Long count){
        if (totalCount == null || totalCount == 0) return 0.0;
        return ((double) count / totalCount) * 100;
    }

    private List<DashboardRatioResDto> extractedFieldDto(Long totalCount, Dashboard dashboard) {
        return List.of(
                new DashboardRatioResDto("앱프론트", totalCount, getRatio(totalCount, dashboard.getAppFrontCount())),
                new DashboardRatioResDto("웹프론트", totalCount, getRatio(totalCount, dashboard.getWebFrontCount())),
                new DashboardRatioResDto("백엔드", totalCount, getRatio(totalCount, dashboard.getBackendCount())),
                new DashboardRatioResDto("디자인", totalCount, getRatio(totalCount, dashboard.getDesignCount())),
                new DashboardRatioResDto("데이터분석", totalCount, getRatio(totalCount, dashboard.getDataAnalysisCount())),
                new DashboardRatioResDto("딥러닝", totalCount, getRatio(totalCount, dashboard.getDeepCount()))
        );
    }

    private List<DashboardRatioResDto> extractedSexDto(Long totalCount, Dashboard dashboard) {
        return List.of(
                new DashboardRatioResDto("남성", totalCount, getRatio(totalCount, dashboard.getMaleCount())),
                new DashboardRatioResDto("여성", totalCount, getRatio(totalCount, dashboard.getFemaleCount()))
        );
    }
}
