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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if(resumeEvaluationRepository.existsByMemberIdAndResumeId(currentMember.getId(), resumeId)){
            throw new AlreadyExistsResumeException();
        }
        ResumeEvaluation resumeEvaluation = ResumeEvaluation.of(resumeEvaluateReqDto, currentMember, resume);
        resumeEvaluationRepository.save(resumeEvaluation);
        resume.updateChecked(true);
    }

    //본인 기반의 작성한 지원서에 대해 조회해야됨
    @Transactional(readOnly = true)
    public ResumeEvaluateResDto getResumes(Pageable pageable) {
        Page<Resume> resumes = resumeRepository.findAll(pageable);

        Page<ResumeResDto> resumeDtos = resumes.map(
                ResumeResDto::from
        );

        return ResumeEvaluateResDto.fromResume(resumeRepository.count(),
                resumeRepository.countByHasChecked(Boolean.FALSE),
                resumeRepository.countByHasChecked(Boolean.TRUE),
                resumeDtos);
    }

    private Resume findIfResumeExists(Long resumeId) {
        return resumeRepository.findById(resumeId).orElseThrow(ResumeNotFoundException::new);
    }
}
