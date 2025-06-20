package com.tave.tavewebsite.domain.apply.initial.setup.service;

import com.tave.tavewebsite.domain.apply.initial.setup.dto.request.ApplyInitialSetupRequestDto;
import com.tave.tavewebsite.domain.apply.initial.setup.dto.response.ApplyInitialSetupReadResponseDto;
import com.tave.tavewebsite.domain.apply.initial.setup.entity.ApplyInitialSetup;
import com.tave.tavewebsite.domain.apply.initial.setup.exception.ApplyInitialSetupException.ApplyInitialSetupNotFoundException;
import com.tave.tavewebsite.domain.apply.initial.setup.repository.ApplyInitialSetupRepository;
import com.tave.tavewebsite.domain.apply.initial.setup.util.ApplyInitialSetUpMapper;
import com.tave.tavewebsite.domain.resume.batch.exception.RecruitmentBatchJobException.DocumentResultBatchJobFailException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplyInitialSetupService {

    private final ApplyInitialSetupRepository applyInitialSetupRepository;

    @Transactional(readOnly = true)
    public ApplyInitialSetupReadResponseDto getInitialSetup() {
        ApplyInitialSetup applyInitialSetup = applyInitialSetupRepository.findById(1L)
                .orElseThrow(ApplyInitialSetupNotFoundException::new);
        return ApplyInitialSetUpMapper.toApplyInitialSetupReadResponseDto(applyInitialSetup);
    }

    public void saveInitialSetup(ApplyInitialSetupRequestDto dto) {
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

    public void changeDocumentAnnouncementFlag(Boolean flag) {
        ApplyInitialSetup applyInitialSetup = applyInitialSetupRepository.findById(1L)
                .orElseThrow(DocumentResultBatchJobFailException::new);
        applyInitialSetup.changeDocumentAnnouncementFlag(flag);
    }

    public void changeLastAnnouncementFlag(Boolean flag) {
        ApplyInitialSetup applyInitialSetup = applyInitialSetupRepository.findById(1L)
                .orElseThrow(DocumentResultBatchJobFailException::new);
        applyInitialSetup.changeLastAnnouncementFlag(flag);
    }
}
