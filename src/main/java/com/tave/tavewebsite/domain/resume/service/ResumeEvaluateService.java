package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.dto.request.DocumentEvaluationReqDto;
import com.tave.tavewebsite.domain.resume.dto.request.FinalDocumentEvaluationReqDto;
import com.tave.tavewebsite.domain.resume.dto.response.DocumentEvaluationResDto;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeEvaluateResDto;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeResDto;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeEvaluation;
import com.tave.tavewebsite.domain.resume.entity.ResumeState;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
import com.tave.tavewebsite.domain.resume.repository.ResumeEvaluationRepository;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.global.common.FieldType;
import com.tave.tavewebsite.global.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeEvaluateService {

    private final ResumeEvaluationRepository resumeEvaluationRepository;
    private final ResumeRepository resumeRepository;

    public Member getCurrentMember() {
        return SecurityUtils.getCurrentMember();
    }

    @Transactional
    public void createDocumentEvaluation(Long resumeId, DocumentEvaluationReqDto documentEvaluationReqDto) {
        Resume resume = findIfResumeExists(resumeId);
        Member currentMember = getCurrentMember();

        if (resumeEvaluationRepository.existsByMemberIdAndResumeId(currentMember.getId(), resumeId)) {
            ResumeEvaluation evaluation = resumeEvaluationRepository.findByMemberIdAndResumeId(currentMember.getId(), resumeId);
            evaluation.update(documentEvaluationReqDto);
        } else {
            ResumeEvaluation resumeEvaluation = ResumeEvaluation.of(documentEvaluationReqDto, currentMember, resume);
            resumeEvaluationRepository.save(resumeEvaluation);
        }
    }

    @Transactional(readOnly = true)
    public DocumentEvaluationResDto getDocumentEvaluation(Member currentMember, Long resumeId) {
        findIfResumeExists(resumeId);
        ResumeEvaluation evaluation = resumeEvaluationRepository.findByMemberIdAndResumeId(currentMember.getId(), resumeId);

        return DocumentEvaluationResDto.from(evaluation);
    }

    //본인 기반의 작성한 지원서에 대해 조회해야됨
    @Transactional(readOnly = true)
    public ResumeEvaluateResDto getDocumentResumes(EvaluationStatus status, FieldType type, String name, Pageable pageable) {
        Member currentMember = getCurrentMember();
        Page<ResumeResDto> resumeResDtos = resumeRepository.findMiddleEvaluation(currentMember, status, type, name, pageable);

        return ResumeEvaluateResDto.fromResume(
                resumeRepository.countByState(ResumeState.SUBMITTED),
                resumeRepository.findNotEvaluatedResume(currentMember),
                resumeRepository.findEvaluatedResume(currentMember),
                resumeResDtos);
    }

    @Transactional(readOnly = true)
    public ResumeEvaluateResDto getFinalDocumentResumes(EvaluationStatus status, FieldType type, String name, Pageable pageable) {
        Member currentMember = getCurrentMember();
        Page<ResumeResDto> resumeResDtos =
                resumeRepository.findFinalEvaluation(currentMember, status, type, name, pageable);

        return ResumeEvaluateResDto.fromResume(
                resumeRepository.countByState(ResumeState.SUBMITTED),
                resumeRepository.countByStateAndFinalDocumentEvaluationStatus(ResumeState.SUBMITTED, EvaluationStatus.NOTCHECKED),
                resumeRepository.countByStateAndFinalDocumentEvaluationStatus(ResumeState.SUBMITTED, EvaluationStatus.PASS),
                resumeResDtos);
    }

    @Transactional
    public void createFinalDocumentEvaluation(Long resumeId, FinalDocumentEvaluationReqDto reqDto) {
        Resume resume = findIfResumeExists(resumeId);
        resume.updateFinalDocumentEvaluationStatus(reqDto.status());
    }

    @Transactional(readOnly = true)
    public List<DocumentEvaluationResDto> getDocumentEvaluations(Long resumeId) {
        Resume resume = findIfResumeExists(resumeId);
        List<ResumeEvaluation> resumeEvaluations =
                resumeEvaluationRepository.findByResumeIdAndFinalEvaluateDocument(resumeId, EvaluationStatus.COMPLETE);

        return resumeEvaluations.stream().map(
                DocumentEvaluationResDto::from
        ).toList();
    }

    @Transactional
    public void deleteAllResume() {
        resumeRepository.deleteAll();
    }

    private Resume findIfResumeExists(Long resumeId) {
        return resumeRepository.findById(resumeId).orElseThrow(ResumeNotFoundException::new);
    }
}
