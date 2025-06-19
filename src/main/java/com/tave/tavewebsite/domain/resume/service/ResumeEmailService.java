package com.tave.tavewebsite.domain.resume.service;


import com.tave.tavewebsite.domain.apply.initial.setup.entity.ApplyInitialSetup;
import com.tave.tavewebsite.domain.apply.initial.setup.exception.ApplyInitialSetupException.ApplyInitialSetupNotFoundException;
import com.tave.tavewebsite.domain.apply.initial.setup.repository.ApplyInitialSetupRepository;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.global.mail.service.SESMailService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ResumeEmailService {

    private final ResumeRepository resumeRepository;
    private final SESMailService sesMailService;
    private final ApplyInitialSetupRepository applyInitialSetupRepository;

    @Transactional(readOnly = true)
    public void sendResumeEmail(Long id) {
        Resume resume = resumeRepository.findById(id).orElseThrow(ResumeNotFoundException::new);
        ApplyInitialSetup applyInitialSetup = applyInitialSetupRepository.findById(1L)
                .orElseThrow(ApplyInitialSetupNotFoundException::new);

        sesMailService.sendApplySubmitMail(resume.getMember().getEmail(), resume.getMember().getUsername(),
                resume.getResumeGeneration(), resume.getField(), LocalDateTime.now(),
                applyInitialSetup.getDocumentAnnouncementDate());
    }
}
