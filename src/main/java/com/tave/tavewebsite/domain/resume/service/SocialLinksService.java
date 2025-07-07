package com.tave.tavewebsite.domain.resume.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeReqDto;
import com.tave.tavewebsite.domain.resume.dto.request.SocialLinksRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.SocialLinksResponseDto;
import com.tave.tavewebsite.domain.resume.dto.wrapper.ResumeTempWrapper;
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
    private final ResumeAnswerTempService resumeAnswerTempService;

    public SocialLinksService(ResumeRepository resumeRepository, RedisUtil redisUtil, ObjectMapper objectMapper, ResumeAnswerTempService resumeAnswerTempService) {
        this.resumeRepository = resumeRepository;
        this.redisUtil = redisUtil;
        this.objectMapper = objectMapper;
        this.resumeAnswerTempService = resumeAnswerTempService;
    }

    // 소셜 링크 등록
//    @Transactional
//    public void createSocialLinks(Long resumeId, SocialLinksRequestDto socialLinksRequestDto) {
//        Resume resume = resumeRepository.findById(resumeId)
//                .orElseThrow(ResumeNotFoundException::new);
//
//        resume.updateSocialLinks(socialLinksRequestDto); // 이미 존재하는 resume 객체에 소셜 링크 정보 추가
//    }

    // ResumeTempWrapper에 접근해서 page3 갱신하는 로직
    public void createSocialLinks(Long resumeId, SocialLinksRequestDto dto) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);

        resume.updateSocialLinks(dto);

        // Redis에서 기존 temp wrapper 조회
        ResumeTempWrapper existing = resumeAnswerTempService.getTempSavedAnswers(resumeId);

        ResumeReqDto oldPage3 = existing.getPage3();

        // 기존 page3의 타임슬롯 유지 + 소셜 링크만 갱신
        ResumeReqDto updatedPage3 = new ResumeReqDto(
                oldPage3 != null ? oldPage3.answers() : null,
                oldPage3 != null ? oldPage3.timeSlots() : null,
                null,
                dto.getGithubUrl(),
                dto.getBlogUrl(),
                oldPage3 != null ? oldPage3.portfolioUrl() : null // 포폴은 아래에서 따로 처리
        );

        resumeAnswerTempService.tempSaveAnswers(resumeId, 3, updatedPage3);
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
                    return new SocialLinksResponseDto(dto.getBlogUrl(), dto.getGithubUrl(), portfolioUrl);
                } catch (Exception parseError) {
                    // JSON 파싱 실패 → fallback
                }
            }
        } catch (Exception redisError) {
            // Redis 접근 실패 → fallback
        }

        // DB fallback
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);

        // Redis에 다시 저장
        SocialLinksRequestDto fallbackDto = new SocialLinksRequestDto(
                resume.getBlogUrl(), resume.getGithubUrl()
        );
        saveSocialLinksToRedis(resumeId, fallbackDto);

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
    public void updateSocialLinks(Long resumeId, SocialLinksRequestDto dto) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);

        resume.updateSocialLinks(dto);

        ResumeReqDto page3 = new ResumeReqDto(null, null, null,
                dto.getGithubUrl(), dto.getBlogUrl(), null);

        resumeAnswerTempService.tempSaveAnswers(resumeId, 3, page3); // Redis에도 저장
    }


    public void updatePortfolio(Long resumeId, String portfolioUrl) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);

        resume.updatePortfolio(portfolioUrl);
        resumeRepository.save(resume);

        // Redis에서 기존 temp wrapper 조회
        ResumeTempWrapper existing = resumeAnswerTempService.getTempSavedAnswers(resumeId);
        ResumeReqDto oldPage3 = existing.getPage3();

        ResumeReqDto updatedPage3 = new ResumeReqDto(
                oldPage3 != null ? oldPage3.answers() : null,
                oldPage3 != null ? oldPage3.timeSlots() : null,
                null,
                oldPage3 != null ? oldPage3.githubUrl() : null,
                oldPage3 != null ? oldPage3.blogUrl() : null,
                portfolioUrl // 새로운 포폴 주소
        );

        resumeAnswerTempService.tempSaveAnswers(resumeId, 3, updatedPage3);
    }


    // Redis에 임시 저장
    public void saveSocialLinksToRedis(Long resumeId, SocialLinksRequestDto dto) {
        try {
            String key = "resume:" + resumeId + ":socialLinks";
            String value = objectMapper.writeValueAsString(dto);
            redisUtil.set(key, value, 2592000);
        } catch (Exception e) {
            throw new TempSaveFailedException();
        }
    }

    public void savePortfolioToRedis(Long resumeId, String portfolioUrl) {
        try {
            String key = "resume:" + resumeId + ":portfolioUrl";
            redisUtil.set(key, portfolioUrl, 2592000);
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
