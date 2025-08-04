package com.tave.tavewebsite.domain.resume.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.tavewebsite.domain.programinglaunguage.dto.request.LanguageLevelRequestDto;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeAnswerTempDto;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeReqDto;
import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotReqDto;
import com.tave.tavewebsite.domain.resume.dto.wrapper.ResumeTempWrapper;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeQuestion;
import com.tave.tavewebsite.domain.resume.exception.*;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.global.common.FieldType;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeAnswerTempService {

    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;
    private final ResumeRepository resumeRepository;

    private String getRedisKey(Long resumeId) {
        String REDIS_KEY_PREFIX = "resume:temp:";
        return REDIS_KEY_PREFIX + resumeId;
    }

    private ResumeAnswerTempDto toDto(ResumeQuestion question) {
        return new ResumeAnswerTempDto(
                question.getId(),
                question.getAnswer()
        );
    }

    @Transactional
    public void tempSaveAnswers(Long resumeId, int page, ResumeReqDto dto, Long memberId) {
        validateResumeOwnership(resumeId, memberId);

        String key = getRedisKey(resumeId);

        ResumeTempWrapper wrapper;
        try {
            String existing = (String) redisUtil.get(key);
            if (existing != null) {
                wrapper = objectMapper.readValue(existing, ResumeTempWrapper.class);
                if (!Objects.equals(wrapper.getMemberId(), memberId)) {
                    throw new InvalidDataOwnerException();
                }
            } else {
                wrapper = ResumeTempWrapper.empty(memberId);
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

    public ResumeTempWrapper getTempSavedAnswers(Long resumeId, Long memberId) {
        String key = getRedisKey(resumeId);

        try {
            String json = (String) redisUtil.get(key);
            if (json != null && !json.isBlank()) {
                ResumeTempWrapper wrapper = objectMapper.readValue(json, ResumeTempWrapper.class);

                // 캐시에 저장된 memberId와 요청 memberId가 다르면 예외
                if (!Objects.equals(wrapper.getMemberId(), memberId)) {
                    throw new InvalidDataOwnerException();
                }

                return fillEmptyPagesIfNeeded(wrapper, memberId);
            }
        } catch (InvalidDataOwnerException e) {
            throw e;
        } catch (Exception e) {
            // Redis 접근 실패 등은 무시하고 DB fallback
        }

        // Redis에 캐시 없거나 오류 시 DB에서 조회
        ResumeTempWrapper wrapperFromDb = loadFromDatabase(resumeId, memberId);
        if (wrapperFromDb == null) {
            wrapperFromDb = ResumeTempWrapper.empty(memberId);
        }

        try {
            String json = objectMapper.writeValueAsString(wrapperFromDb);
            redisUtil.set(key, json, 2592000); // TTL 30일
        } catch (Exception ignored) {
        }

        return fillEmptyPagesIfNeeded(wrapperFromDb, memberId);
    }

    private ResumeTempWrapper fillEmptyPagesIfNeeded(ResumeTempWrapper wrapper, Long memberId) {
        if (wrapper.getMemberId() == null) {
            wrapper.setMemberId(memberId);
        }
        if (wrapper.getPage2() == null) {
            wrapper.setPage2(ResumeReqDto.empty());
        }
        if (wrapper.getPage3() == null) {
            wrapper.setPage3(ResumeReqDto.empty());
        }
        return wrapper;
    }

    private ResumeTempWrapper loadFromDatabase(Long resumeId, Long memberId) {
        Resume resume = resumeRepository.findByIdWithTimeSlotsAndMember(resumeId)
                .orElse(null);

        if (resume == null) {
            return null;
        }

        if (!resume.getMember().getId().equals(memberId)) {
            throw new InvalidDataOwnerException();
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
        boolean hasPage2Data = page2Answers.stream().anyMatch(a -> a.getAnswer() != null && !a.getAnswer().isBlank())
                || !languageLevels.isEmpty();

        if (hasPage2Data) {
            ResumeReqDto page2Dto = new ResumeReqDto(
                    page2Answers,
                    new ArrayList<>(),
                    languageLevels
            );
            wrapper.setPage2(page2Dto);
        }

        // 공통 질문 → page3 세팅
        boolean hasPage3Data = page3Answers.stream().anyMatch(a -> a.getAnswer() != null && !a.getAnswer().isBlank())
                || !timeSlots.isEmpty();

        if (hasPage3Data) {
            ResumeReqDto page3Dto = new ResumeReqDto(
                    page3Answers,
                    timeSlots,
                    new ArrayList<>()
            );
            wrapper.setPage3(page3Dto);
        }

        // 마지막 저장 페이지 계산
        int lastPage = 1;
        if (wrapper.getPage2() != null) lastPage = 2;
        if (wrapper.getPage3() != null) lastPage = 3;
        wrapper.setLastPage(lastPage);
        wrapper.setMemberId(memberId);
        return wrapper;
    }

    private void validateResumeOwnership(Long resumeId, Long memberId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);

        if (!resume.getMember().getId().equals(memberId)) {
            throw new InvalidDataOwnerException();
        }
    }

}