package com.tave.tavewebsite.domain.resume.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.tavewebsite.domain.programinglaunguage.dto.request.LanguageLevelRequestDto;
import com.tave.tavewebsite.domain.programinglaunguage.service.ProgramingLanguageService;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeAnswerTempDto;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeReqDto;
import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotReqDto;
import com.tave.tavewebsite.domain.resume.dto.wrapper.ResumeTempWrapper;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeQuestion;
import com.tave.tavewebsite.domain.resume.exception.InvalidPageNumberException;
import com.tave.tavewebsite.domain.resume.exception.TempReadFailedException;
import com.tave.tavewebsite.domain.resume.exception.TempSaveFailedException;
import com.tave.tavewebsite.domain.resume.repository.ResumeQuestionRepository;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.global.common.FieldType;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeAnswerTempService {
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;
    private final ResumeQuestionRepository resumeQuestionRepository;
    private final ResumeRepository resumeRepository;
    private final InterviewTimeService interviewTimeService;
    private final ProgramingLanguageService programingLanguageService;


    private final String REDIS_KEY_PREFIX = "resume:temp:";

    private ResumeAnswerTempDto toDto(ResumeQuestion question) {
        return new ResumeAnswerTempDto(
                question.getId(),
                question.getAnswer()
        );
    }

    @Transactional
    public void tempSaveAnswers(Long resumeId, int page, ResumeReqDto dto) {
        String key = REDIS_KEY_PREFIX + resumeId;

        ResumeTempWrapper wrapper;
        try {
            String existing = (String) redisUtil.get(key);
            if (existing != null) {
                wrapper = objectMapper.readValue(existing, ResumeTempWrapper.class);
            } else {
                wrapper = new ResumeTempWrapper();
            }
        } catch (Exception e) {
            throw new TempReadFailedException();
        }

        // 해당 페이지에 맞는 필드만 저장
        switch (page) {
            case 2 -> wrapper.setPage2(dto);
            case 3 -> wrapper.setPage3(dto);
            default -> throw new InvalidPageNumberException();
        }

        wrapper.setLastPage(page);

        try {
            String json = objectMapper.writeValueAsString(wrapper);
            redisUtil.set(key, json, 2592000); // TTL 30일
        } catch (Exception e) {
            throw new TempSaveFailedException();
        }
    }

    public ResumeTempWrapper getTempSavedAnswers(Long resumeId) {
        String key = REDIS_KEY_PREFIX + resumeId;

        ResumeTempWrapper wrapper = null;

        try {
            String json = (String) redisUtil.get(key);

            // Redis 값이 존재하고, 비어있지 않으면 파싱 시도
            if (json != null && !json.isBlank()) {
                try {
                    wrapper = objectMapper.readValue(json, ResumeTempWrapper.class);
                } catch (Exception jsonEx) {
                    // 파싱 실패 시 wrapper는 그대로 null → DB fallback
                }
            }

            // Redis에 값이 없거나 파싱 실패한 경우 → DB 조회
            if (wrapper == null) {
                wrapper = loadFromDatabase(resumeId);

                if (wrapper != null) {
                    try {
                        String dbJson = objectMapper.writeValueAsString(wrapper);
                        redisUtil.set(key, dbJson, 2592000);
                    } catch (Exception e) {
                        // Redis 저장 실패 → 무시하고 wrapper만 반환
                    }
                }
            }

            // DB에도 없으면 → 기본 빈 wrapper
            if (wrapper == null) {
                wrapper = new ResumeTempWrapper();
                wrapper.setLastPage(1);
            }

            return wrapper;

        } catch (Exception e) {
            // Redis 접근 자체가 실패한 경우 → 기본 빈 wrapper 반환
            ResumeTempWrapper fallback = new ResumeTempWrapper();
            fallback.setLastPage(1);
            return fallback;
        }
    }

    private ResumeTempWrapper loadFromDatabase(Long resumeId) {
        Resume resume = resumeRepository.findWithAllRelationsById(resumeId).orElse(null);
        if (resume == null) {
            return null;
        }

        ResumeTempWrapper wrapper = new ResumeTempWrapper();

        // 모든 질문+답변 조회
        List<ResumeQuestion> allQuestions = resume.getResumeQuestions();

        // page 2: 분야별 질문 (fieldType != COMMON)
        List<ResumeAnswerTempDto> page2Answers = allQuestions.stream()
                .filter(q -> q.getFieldType() != FieldType.COMMON)
                .map(this::toDto)
                .toList();

        // page 3: 공통 질문 (fieldType == COMMON)
        List<ResumeAnswerTempDto> page3Answers = allQuestions.stream()
                .filter(q -> q.getFieldType() == FieldType.COMMON)
                .map(this::toDto)
                .toList();

        String githubUrl = resume.getGithubUrl();
        String blogUrl = resume.getBlogUrl();
        String portfolioUrl = resume.getPortfolioUrl();

        List<TimeSlotReqDto> timeSlots = resume.getResumeTimeSlots().stream()
                .map(slot -> new TimeSlotReqDto(slot.getInterviewDetailTime()))
                .collect(Collectors.toList());
        List<LanguageLevelRequestDto> languageLevels = resume.getLanguageLevels().stream()
                .map(LanguageLevelRequestDto::fromEntity)
                .collect(Collectors.toList());

        // 분야별 질문 → page2 세팅
        if (!page2Answers.isEmpty() || !languageLevels.isEmpty()) {
            ResumeReqDto page2Dto = new ResumeReqDto(
                    page2Answers,
                    null,
                    languageLevels,
                    null, null, null
            );
            wrapper.setPage2(page2Dto);
        }

        // 공통 질문 → page3 세팅
        if (!page3Answers.isEmpty() || timeSlots.isEmpty() || githubUrl != null || blogUrl != null || portfolioUrl != null) {
            ResumeReqDto page3Dto = new ResumeReqDto(
                    page3Answers,
                    timeSlots,
                    null,
                    githubUrl,
                    blogUrl,
                    portfolioUrl
            );
            wrapper.setPage3(page3Dto);
        }

        // 마지막 저장 페이지 계산
        int lastPage = 1;
        if (wrapper.getPage2() != null) lastPage = 2;
        if (wrapper.getPage3() != null) lastPage = Math.max(lastPage, 3);
        wrapper.setLastPage(lastPage);

        return wrapper;
    }

    public int getLastPage(Long resumeId) {
        ResumeTempWrapper wrapper = getTempSavedAnswers(resumeId);
        return wrapper == null ? 1 : wrapper.getLastPage();
    }
}