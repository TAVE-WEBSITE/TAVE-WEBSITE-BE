package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.resume.dto.request.SocialLinksRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.SocialLinksResponseDto;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.exception.InvalidDataOwnerException;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SocialLinksService {

    private final ResumeRepository resumeRepository;

    public SocialLinksService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    // 소셜 주소 등록
    @Transactional
    public void createSocialLinks(Long resumeId, SocialLinksRequestDto dto, Long memberId) {
        Resume resume = getAuthorizedResume(resumeId, memberId);
        resume.updateSocialLinks(dto);
    }

    public SocialLinksResponseDto getSocialLinks(Long resumeId, Long memberId) {
        Resume resume = getAuthorizedResume(resumeId, memberId);
        return new SocialLinksResponseDto(
                memberId,
                resume.getBlogUrl(),
                resume.getGithubUrl(),
                resume.getPortfolioUrl()
        );
    }

    // 소셜 링크 업데이트
    @Transactional
    public void updateSocialLinks(Long resumeId, SocialLinksRequestDto dto, Long memberId) {
        Resume resume = getAuthorizedResume(resumeId, memberId);
        resume.updateSocialLinks(dto);
    }

    public void updatePortfolio(Long resumeId, String portfolioUrl, Long memberId) {
        Resume resume = getAuthorizedResume(resumeId, memberId);
        resume.updatePortfolio(portfolioUrl);
        resumeRepository.save(resume);
    }

    // Resume 조회 및 소유자 검증을 함께 수행하는 메서드
    private Resume getAuthorizedResume(Long resumeId, Long memberId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);

        if (!resume.getMember().getId().equals(memberId)) {
            throw new InvalidDataOwnerException();
        }
        return resume;
    }

}
