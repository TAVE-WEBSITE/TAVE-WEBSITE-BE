package com.tave.tavewebsite.domain.resume.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.tavewebsite.domain.resume.dto.request.SocialLinksRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.SocialLinksResponseDto;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.TempNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.TempParseFailedException;
import com.tave.tavewebsite.domain.resume.exception.TempSaveFailedException;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SocialLinksService {

    private final ResumeRepository resumeRepository;
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;

    public SocialLinksService(ResumeRepository resumeRepository, RedisUtil redisUtil, ObjectMapper objectMapper) {
        this.resumeRepository = resumeRepository;
        this.redisUtil = redisUtil;
        this.objectMapper = objectMapper;
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
        String portfolioUrl = getPortfolioFromRedisOrDb(resumeId);

        try {
            String redisKey = "resume:" + resumeId + ":socialLinks";
            String redisJson = (String) redisUtil.get(redisKey);

            if (redisJson != null) {
                try {
                    SocialLinksRequestDto dto = objectMapper.readValue(redisJson, SocialLinksRequestDto.class);
                    return new SocialLinksResponseDto(
                            dto.getBlogUrl(), dto.getGithubUrl(), portfolioUrl
                    );
                } catch (Exception parseError) {
                    // Redis 파싱 실패 → DB fallback, parseError;
                }
            }
        } catch (Exception redisError) {
            // Redis 접근 실패 → DB fallback, redisError;
        }

        // DB fallback
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);

        return new SocialLinksResponseDto(
                resume.getBlogUrl(),
                resume.getGithubUrl(),
                portfolioUrl
        );
    }

//    public SocialLinksResponseDto getSocialLinks(Long resumeId) {
//        Resume resume = resumeRepository.findById(resumeId)
//                .orElseThrow(ResumeNotFoundException::new);
//
//        return new SocialLinksResponseDto(resume.getBlogUrl(), resume.getGithubUrl(), resume.getPortfolioUrl());
//    }

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

    // Redis에 임시 저장
    public void saveSocialLinksToRedis(Long resumeId, SocialLinksRequestDto dto) {
        try {
            String key = "resume:" + resumeId + ":socialLinks";
            String value = objectMapper.writeValueAsString(dto);
            redisUtil.set(key, value, 86400); // TTL 1일
        } catch (Exception e) {
            throw new TempSaveFailedException();
        }
    }

    public void savePortfolioToRedis(Long resumeId, String portfolioUrl) {
        try {
            String key = "resume:" + resumeId + ":portfolioUrl";
            redisUtil.set(key, portfolioUrl, 86400); // TTL 1일
        } catch (Exception e) {
            throw new TempSaveFailedException();
        }
    }

    public String getPortfolioFromRedis(Long resumeId) {
        try {
            String key = "resume:" + resumeId + ":portfolioUrl";
            return (String) redisUtil.get(key);
        } catch (Exception e) {
            throw new TempParseFailedException();
        }
    }

    private String getPortfolioFromRedisOrDb(Long resumeId) {
        String key = "resume:" + resumeId + ":portfolioUrl";
        String url = (String) redisUtil.get(key);

        if (url != null) {
            return url;
        }

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);
        String dbUrl = resume.getPortfolioUrl();
        if (dbUrl != null) {
            savePortfolioToRedis(resumeId, dbUrl); // Redis에 캐싱
        }
        return dbUrl;
    }

}
