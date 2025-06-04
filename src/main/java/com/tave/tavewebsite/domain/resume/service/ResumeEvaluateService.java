package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.dto.request.DocumentEvaluationReqDto;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeEvaluateResDto;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeResDto;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
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
    public void createDocumentEvaluation(Long resumeId, DocumentEvaluationReqDto documentEvaluationReqDto){
        Resume resume = findIfResumeExists(resumeId);
        Member currentMember = getCurrentMember();

        if(resumeEvaluationRepository.existsByMemberIdAndResumeId(currentMember.getId(), resumeId)){
            ResumeEvaluation evaluation = resumeEvaluationRepository.findByMemberIdAndResumeId(currentMember.getId(), resumeId);
            evaluation.update(documentEvaluationReqDto);
        }
        else {
            ResumeEvaluation resumeEvaluation = ResumeEvaluation.of(documentEvaluationReqDto, currentMember, resume);
            resumeEvaluationRepository.save(resumeEvaluation);
        }
    }

    //본인 기반의 작성한 지원서에 대해 조회해야됨
    @Transactional(readOnly = true)
    public ResumeEvaluateResDto getDocumentResumes(EvaluationStatus status, Pageable pageable) {
        Member currentMember = getCurrentMember();
        Page<ResumeResDto> resumeResDtos = resumeRepository.findMiddleEvaluation(currentMember, status, pageable);



        return ResumeEvaluateResDto.fromResume(resumeRepository.count(),
                resumeRepository.findNotEvaluatedResume(currentMember),
                resumeRepository.findEvaluatedResume(currentMember),
                resumeResDtos);
    }

    @Transactional(readOnly = true)
    public ResumeEvaluateResDto getFinalDocumentResumes(EvaluationStatus status, Pageable pageable) {
        Member currentMember = getCurrentMember();
        Page<ResumeResDto> resumeResDtos =
                resumeRepository.findFinalEvaluation(currentMember, status, pageable);

        return ResumeEvaluateResDto.fromResume(resumeRepository.count(),
                resumeRepository.countByFinalDocumentEvaluationStatus(EvaluationStatus.NOTCHECKED),
                resumeRepository.countByFinalDocumentEvaluationStatus(EvaluationStatus.PASS),
                resumeResDtos);
    }

    private Resume findIfResumeExists(Long resumeId) {
        return resumeRepository.findById(resumeId).orElseThrow(ResumeNotFoundException::new);
    }
}
