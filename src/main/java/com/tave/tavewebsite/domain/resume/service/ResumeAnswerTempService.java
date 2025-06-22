package com.tave.tavewebsite.domain.resume.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.tavewebsite.domain.programinglaunguage.dto.response.LanguageLevelResponseDto;
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
        try {
            String json = (String) redisUtil.get(key);
            if (json != null) {
                return objectMapper.readValue(json, ResumeTempWrapper.class);
            }

            // 캐시 미스 → DB에서 조회 시도
            ResumeTempWrapper dbData = loadFromDatabase(resumeId);

            if (dbData != null) {
                // Redis에 다시 저장 (Write-through 캐시)
                String dbJson = objectMapper.writeValueAsString(dbData);
                redisUtil.set(key, dbJson, 2592000);
                return dbData;
            }

            // DB에도 데이터 없으면 기본값 세팅 후 반환
            ResumeTempWrapper emptyWrapper = new ResumeTempWrapper();
            emptyWrapper.setLastPage(1);
            // 나머지 필드는 null 또는 기본 상태로 둠

            return emptyWrapper;

        } catch (Exception e) {
            throw new TempReadFailedException();
        }
    }

    private ResumeTempWrapper loadFromDatabase(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId).orElse(null);
        if (resume == null) {
            return null;
        }

        ResumeTempWrapper wrapper = new ResumeTempWrapper();

// 모든 질문+답변 조회
        List<ResumeQuestion> allQuestions = resumeQuestionRepository.findByResumeId(resumeId);

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

        // 소셜 URL
        String githubUrl = resume.getGithubUrl();
        String blogUrl = resume.getBlogUrl();
        String portfolioUrl = resume.getPortfolioUrl();

        // 타임슬롯 (InterviewTimeService에서 가져오기)
        List<TimeSlotReqDto> timeSlots = interviewTimeService.getTimeSlotsByResumeId(resumeId);

        // 프로그래밍 언어 레벨 (ProgramingLanguageService에서 가져오기)
        List<LanguageLevelResponseDto> languageLevels = programingLanguageService.getLanguageLevel(resumeId);

        if (!page2Answers.isEmpty() || timeSlots != null || languageLevels != null || githubUrl != null || blogUrl != null || portfolioUrl != null) {
            wrapper.setPage2(new ResumeReqDto(page2Answers, timeSlots, languageLevels, githubUrl, blogUrl, portfolioUrl));
        }

        if (!page3Answers.isEmpty()) {
            // 공통 질문에 소셜 주소 등이 포함되어야 한다면 여기서도 넣으면 됨
            wrapper.setPage3(new ResumeReqDto(page3Answers, null, null, githubUrl, blogUrl, portfolioUrl));
        }

        int lastPage = 1;
        if (!page2Answers.isEmpty()) lastPage = 2;
        if (!page3Answers.isEmpty()) lastPage = Math.max(lastPage, 3);
        wrapper.setLastPage(lastPage);

        return wrapper;
    }

    public int getLastPage(Long resumeId) {
        ResumeTempWrapper wrapper = getTempSavedAnswers(resumeId);
        return wrapper == null ? 1 : wrapper.getLastPage();
    }
}
