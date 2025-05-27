package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.exception.AlreadyExistsResumeException;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeEvaluateReqDto;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeEvaluateResDto;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeResDto;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeEvaluation;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
import com.tave.tavewebsite.domain.resume.repository.ResumeEvaluationRepository;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.global.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeEvaluateService {

    private final ResumeEvaluationRepository resumeEvaluationRepository;
    private final ResumeRepository resumeRepository;

    public Member getCurrentMember(){
        return SecurityUtils.getCurrentMember();
    }

    @Transactional
    public void createDocumentEvaluation(Long resumeId, ResumeEvaluateReqDto resumeEvaluateReqDto){
        Resume resume = findIfResumeExists(resumeId);
        Member currentMember = getCurrentMember();

        if(resumeEvaluationRepository.existsByMemberId(currentMember.getId())){
            throw new AlreadyExistsResumeException();
        }
        ResumeEvaluation resumeEvaluation = ResumeEvaluation.of(resumeEvaluateReqDto, currentMember, resume);
        resumeEvaluationRepository.save(resumeEvaluation);
        resume.updateChecked(true);
    }

    @Transactional(readOnly = true)
    public ResumeEvaluateResDto getResumes() {
        List<Resume> resumes = resumeRepository.findAll();

        List<ResumeResDto> resumeDtos = resumes.stream().map(
                ResumeResDto::from
        ).toList();

        return ResumeEvaluateResDto.fromResume(resumes.size(),
                resumeRepository.countByHasChecked(Boolean.FALSE),
                resumeRepository.countByHasChecked(Boolean.TRUE),
                resumeDtos);
    }

    private Resume findIfResumeExists(Long resumeId) {
        return resumeRepository.findById(resumeId).orElseThrow(ResumeNotFoundException::new);
    }
}
