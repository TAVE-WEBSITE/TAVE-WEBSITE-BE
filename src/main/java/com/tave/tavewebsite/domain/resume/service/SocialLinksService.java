package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.resume.dto.request.SocialLinksRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.SocialLinksResponseDto;
import com.tave.tavewebsite.domain.resume.entity.Resume;
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

    // 소셜 링크 등록
    @Transactional
    public void createSocialLinks(Long resumeId, SocialLinksRequestDto socialLinksRequestDto) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);

        resume.updateSocialLinks(socialLinksRequestDto); // 이미 존재하는 resume 객체에 소셜 링크 정보 추가
    }

    // 소셜 링크 조회
    public SocialLinksResponseDto getSocialLinks(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);

        return new SocialLinksResponseDto(resume.getBlogUrl(), resume.getGithubUrl(), resume.getPortfolioUrl());
    }

    // 소셜 링크 업데이트
    @Transactional
    public void updateSocialLinks(Long resumeId, SocialLinksRequestDto socialLinksRequestDto) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);

        resume.updateSocialLinks(socialLinksRequestDto);
    }

    @Transactional
    public void updatePortfolio(Long resumeId, String portfolioUrl) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);

        resume.updatePortfolio(portfolioUrl); // Resume 엔티티에 메서드 추가 필요
    }
}
