package com.tave.tavewebsite.domain.resume.service;


import com.tave.tavewebsite.domain.applicant.history.entity.ApplicantHistory;
import com.tave.tavewebsite.domain.applicant.history.entity.ApplicationStatus;
import com.tave.tavewebsite.domain.apply.initial.setup.entity.ApplyInitialSetup;
import com.tave.tavewebsite.domain.apply.initial.setup.exception.ApplyInitialSetupException.ApplyInitialSetupNotFoundException;
import com.tave.tavewebsite.domain.apply.initial.setup.repository.ApplyInitialSetupRepository;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.global.mail.service.SESMailService;
import java.time.LocalDateTime;
import java.util.List;
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

    public void sendResumeEmail(Long id) {
        Resume resume = resumeRepository.findById(id).orElseThrow(ResumeNotFoundException::new);
        ApplyInitialSetup applyInitialSetup = applyInitialSetupRepository.findById(1L)
                .orElseThrow(ApplyInitialSetupNotFoundException::new);

        List<ApplicantHistory> applicantHistories = resume.getMember().getApplicantHistories();

        for (ApplicantHistory applicantHistory : applicantHistories) {
            if (applicantHistory.getGeneration().equals(applyInitialSetup.getGeneration())) {
                applicantHistory.updateStatus(ApplicationStatus.SUBMITTED);
            }
        }

        sesMailService.sendApplySubmitMail(resume.getMember().getEmail(), resume.getMember().getUsername(),
                resume.getResumeGeneration(), resume.getField(), LocalDateTime.now(),
                applyInitialSetup.getDocumentAnnouncementDate());
    }
}
