package com.tave.tavewebsite.domain.apply.initial.setup.service;

import com.tave.tavewebsite.domain.apply.dashboard.service.DashboardService;
import com.tave.tavewebsite.domain.apply.initial.setup.dto.request.ApplyInitialSetupRequestDto;
import com.tave.tavewebsite.domain.apply.initial.setup.dto.response.ApplyInitialSetupReadResponseDto;
import com.tave.tavewebsite.domain.apply.initial.setup.entity.ApplyInitialSetup;
import com.tave.tavewebsite.domain.apply.initial.setup.exception.ApplyInitialSetupException.ApplyInitialSetupNotFoundException;
import com.tave.tavewebsite.domain.apply.initial.setup.repository.ApplyInitialSetupRepository;
import com.tave.tavewebsite.domain.apply.initial.setup.util.ApplyInitialSetUpMapper;
import com.tave.tavewebsite.domain.resume.batch.dto.response.RecruitmentResponseDto;
import com.tave.tavewebsite.domain.resume.batch.exception.RecruitmentBatchJobException.DocumentResultBatchJobFailException;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ApplyInitialSetupService {

    private final ApplyInitialSetupRepository applyInitialSetupRepository;
    private final DashboardService dashboardService;
    private final ResumeRepository resumeRepository;

    @Transactional(readOnly = true)
    public ApplyInitialSetupReadResponseDto getInitialSetup() {
        ApplyInitialSetup applyInitialSetup = applyInitialSetupRepository.findById(1L)
                .orElseThrow(ApplyInitialSetupNotFoundException::new);
        return ApplyInitialSetUpMapper.toApplyInitialSetupReadResponseDto(applyInitialSetup);
    }

    @Transactional(readOnly = true)
    public RecruitmentResponseDto getDocumentAnnouncementFlag() {
        ApplyInitialSetup applyInitialSetup = applyInitialSetupRepository.findById(1L)
                .orElseThrow(ApplyInitialSetupNotFoundException::new);
        return new RecruitmentResponseDto(applyInitialSetup.getDocumentAnnouncementFlag());
    }

    @Transactional(readOnly = true)
    public RecruitmentResponseDto getLastAnnouncementFlag() {
        ApplyInitialSetup applyInitialSetup = applyInitialSetupRepository.findById(1L)
                .orElseThrow(ApplyInitialSetupNotFoundException::new);
        return new RecruitmentResponseDto(applyInitialSetup.getLastAnnouncementFlag());
    }

    public void saveInitialSetup(ApplyInitialSetupRequestDto dto) {
        dashboardService.initDashboard(); // 대시보드 초기화
        if (applyInitialSetupRepository.existsById(1L)) {
            Optional<ApplyInitialSetup> byId = applyInitialSetupRepository.findById(1L);
            byId.get().updateFrom(dto);
            return;
        }
        ApplyInitialSetup entity = ApplyInitialSetUpMapper.toEntity(dto);
        applyInitialSetupRepository.save(entity);
    }

    public void updateInitialSetup(ApplyInitialSetupRequestDto dto) {
        ApplyInitialSetup existing = applyInitialSetupRepository.findById(1L)
                .orElseThrow(ApplyInitialSetupNotFoundException::new);

        existing.updateFrom(dto);
    }

    public void deleteInitialSetup() {
        if (!applyInitialSetupRepository.existsById(1L)) {
            throw new ApplyInitialSetupNotFoundException();
        }

        applyInitialSetupRepository.deleteById(1L);
    }

    public boolean checkRecruitExpiration() {
        ApplyInitialSetup applyInitialSetup = applyInitialSetupRepository.findById(1L)
                .orElseThrow(ApplyInitialSetupNotFoundException::new);

        return applyInitialSetup.isOverDocumentEndDate();
    }

    public boolean checkRecruitStartDate() {
        ApplyInitialSetup applyInitialSetup = applyInitialSetupRepository.findById(1L)
                .orElseThrow(ApplyInitialSetupNotFoundException::new);

        return applyInitialSetup.isOverRecruitStartDate();
    }

    public void changeDocumentAnnouncementFlag(Boolean flag, HttpServletRequest request) {
        ApplyInitialSetup applyInitialSetup = applyInitialSetupRepository.findById(1L)
                .orElseThrow(DocumentResultBatchJobFailException::new);
        log.info("서류 평가 완료 이메일 전송 예약 완료, 실행자 ip : {}, 기기 종류 : {}", request.getRemoteAddr(),
                request.getHeader("User-Agent"));
        applyInitialSetup.changeDocumentAnnouncementFlag(flag);
    }

    public void changeLastAnnouncementFlag(Boolean flag, HttpServletRequest request) {
        ApplyInitialSetup applyInitialSetup = applyInitialSetupRepository.findById(1L)
                .orElseThrow(DocumentResultBatchJobFailException::new);
        log.info("최종 평가 완료 이메일 전송 flag 변경 : {}, 실행자 ip : {}, 기기 종류 : {}", flag, request.getRemoteAddr(),
                request.getHeader("User-Agent"));
        applyInitialSetup.changeLastAnnouncementFlag(flag);
    }

    public String getCurrentGeneration() {
        ApplyInitialSetup applyInitialSetup = applyInitialSetupRepository.findById(1L)
                .orElseThrow(ApplyInitialSetupNotFoundException::new);
        log.info("현재 기수 = {}", applyInitialSetup.getGeneration());
        return applyInitialSetup.getGeneration();
    }
}
