package com.tave.tavewebsite.domain.resume.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeReqDto;
import com.tave.tavewebsite.domain.resume.dto.request.SocialLinksRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.SocialLinksResponseDto;
import com.tave.tavewebsite.domain.resume.dto.wrapper.ResumeTempWrapper;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.exception.InvalidDataOwnerException;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
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

    // ResumeTempWrapper에 접근해서 page3 갱신하는 로직
    public void createSocialLinks(Long resumeId, SocialLinksRequestDto dto, Long memberId) {
        Resume resume = getAuthorizedResume(resumeId, memberId);

        resume.updateSocialLinks(dto);

        // Redis에서 기존 temp wrapper 조회
        updatePage3WithPreservedTimeSlots(resumeId, memberId);
    }

    // 소셜 링크 조회
    public SocialLinksResponseDto getSocialLinks(Long resumeId, Long memberId) {
        String portfolioUrl = getPortfolioFromRedisOrDb(resumeId, memberId);

        try {
            String redisKey = "resume:" + resumeId + ":socialLinks";
            String redisJson = (String) redisUtil.get(redisKey);

            if (redisJson != null) {
                try {
                    SocialLinksRequestDto dto = objectMapper.readValue(redisJson, SocialLinksRequestDto.class);
                    getAuthorizedResume(resumeId, memberId);
                    return new SocialLinksResponseDto(memberId, dto.getBlogUrl(), dto.getGithubUrl(), portfolioUrl);
                } catch (Exception parseError) {
                    // JSON 파싱 실패 → fallback
                }
            }
        } catch (Exception redisError) {
            // Redis 접근 실패 → fallback
        }

        // DB fallback
        Resume resume = getAuthorizedResume(resumeId, memberId);

        // Redis에 다시 저장
        SocialLinksRequestDto fallbackDto = new SocialLinksRequestDto(
                resume.getBlogUrl(), resume.getGithubUrl()
        );
        saveSocialLinksToRedis(resumeId, fallbackDto, memberId);

        return new SocialLinksResponseDto(
                memberId,
                resume.getBlogUrl(),
                resume.getGithubUrl(),
                portfolioUrl
        );
    }

    // 소셜 링크 업데이트
    @Transactional
    public void updateSocialLinks(Long resumeId, SocialLinksRequestDto dto, Long memberId) {
        Resume resume = getAuthorizedResume(resumeId, memberId);

        resume.updateSocialLinks(dto);

        ResumeReqDto page3 = new ResumeReqDto(null, null, null);

        resumeAnswerTempService.tempSaveAnswers(resumeId, 3, page3, memberId); // Redis에도 저장
    }

    public void updatePortfolio(Long resumeId, String portfolioUrl, Long memberId) {
        Resume resume = getAuthorizedResume(resumeId, memberId);

        resume.updatePortfolio(portfolioUrl);
        resumeRepository.save(resume);

        // Redis에서 기존 temp wrapper 조회
        updatePage3WithPreservedTimeSlots(resumeId, memberId);
    }

    // Redis에 임시 저장
    public void saveSocialLinksToRedis(Long resumeId, SocialLinksRequestDto dto, Long memberId) {
        try {
            String key = "resume:" + resumeId + ":member:" + memberId + ":socialLinks";
            String value = objectMapper.writeValueAsString(dto);
            redisUtil.set(key, value, 2592000);
        } catch (Exception e) {
            throw new TempSaveFailedException();
        }
    }

    public void savePortfolioToRedis(Long resumeId, String portfolioUrl, Long memberId) {
        try {
            String key = "resume:" + resumeId + ":member:" + memberId + ":portfolioUrl";
            redisUtil.set(key, portfolioUrl, 2592000);
        } catch (Exception e) {
            throw new TempSaveFailedException();
        }
    }

    private String getPortfolioFromRedisOrDb(Long resumeId, Long memberId) {
        String key = "resume:" + resumeId + ":portfolioUrl";
        String url = (String) redisUtil.get(key);

        if (url != null) {
            return url;
        }

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);
        String dbUrl = resume.getPortfolioUrl();
        if (dbUrl != null) {
            savePortfolioToRedis(resumeId, dbUrl, memberId); // Redis에 캐싱
        }
        return dbUrl;
    }

    private void updatePage3WithPreservedTimeSlots(Long resumeId, Long memberId) {
        ResumeTempWrapper existing = resumeAnswerTempService.getTempSavedAnswers(resumeId, memberId);
        ResumeReqDto oldPage3 = existing.getPage3();

        ResumeReqDto updatedPage3 = new ResumeReqDto(
                oldPage3 != null ? oldPage3.answers() : null,
                oldPage3 != null ? oldPage3.timeSlots() : null,
                null
        );

        resumeAnswerTempService.tempSaveAnswers(resumeId, 3, updatedPage3, memberId);
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
