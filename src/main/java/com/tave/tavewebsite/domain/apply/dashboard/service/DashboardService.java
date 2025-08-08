package com.tave.tavewebsite.domain.apply.dashboard.service;

import com.tave.tavewebsite.domain.apply.dashboard.dto.DashboardRatioResDto;
import com.tave.tavewebsite.domain.apply.dashboard.dto.DashboardResDto;
import com.tave.tavewebsite.domain.apply.dashboard.dto.DashboardUpdateDto;
import com.tave.tavewebsite.domain.apply.dashboard.entity.Dashboard;
import com.tave.tavewebsite.domain.apply.dashboard.exception.DashboardErrorException;
import com.tave.tavewebsite.domain.apply.dashboard.repository.DashboardRepository;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final RedisUtil redisUtil;

    public DashboardResDto getDashboard() {
        Dashboard dashboard = findIfExists();
        double generationRatio = dashboard.getTotalCount() == 0 ? 0.0 :
                (double) (dashboard.getTotalCount() / dashboard.getPreviousCount());
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

    @Transactional
    public void updateDetailedCounts() {
        Dashboard dashboard = findIfExists();

        DashboardUpdateDto dashboardUpdateDto = dashboardRepository.countEachElements();

        dashboard.updateDashboard(dashboardUpdateDto);
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
                new DashboardRatioResDto("앱프론트", dashboard.getAppFrontCount(), getRatio(totalCount, dashboard.getAppFrontCount())),
                new DashboardRatioResDto("웹프론트", dashboard.getWebFrontCount(), getRatio(totalCount, dashboard.getWebFrontCount())),
                new DashboardRatioResDto("백엔드", dashboard.getBackendCount(), getRatio(totalCount, dashboard.getBackendCount())),
                new DashboardRatioResDto("디자인", dashboard.getDesignCount(), getRatio(totalCount, dashboard.getDesignCount())),
                new DashboardRatioResDto("데이터분석", dashboard.getDataAnalysisCount(), getRatio(totalCount, dashboard.getDataAnalysisCount())),
                new DashboardRatioResDto("딥러닝", dashboard.getDeepCount(), getRatio(totalCount, dashboard.getDeepCount()))
        );
    }

    private List<DashboardRatioResDto> extractedSexDto(Long totalCount, Dashboard dashboard) {
        return List.of(
                new DashboardRatioResDto("남성", dashboard.getMaleCount(), getRatio(totalCount, dashboard.getMaleCount())),
                new DashboardRatioResDto("여성", dashboard.getFemaleCount(), getRatio(totalCount, dashboard.getFemaleCount()))
        );
    }
}
