package com.tave.tavewebsite.domain.resume.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeReqDto;
import com.tave.tavewebsite.domain.resume.dto.wrapper.ResumeTempWrapper;
import com.tave.tavewebsite.domain.resume.exception.InvalidPageNumberException;
import com.tave.tavewebsite.domain.resume.exception.TempReadFailedException;
import com.tave.tavewebsite.domain.resume.exception.TempSaveFailedException;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeAnswerTempService {
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;

    private final String REDIS_KEY_PREFIX = "resume:temp:";

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
            if (json == null) return null;
            return objectMapper.readValue(json, ResumeTempWrapper.class);
        } catch (Exception e) {
            throw new TempReadFailedException();
        }
    }

    public int getLastPage(Long resumeId) {
        ResumeTempWrapper wrapper = getTempSavedAnswers(resumeId);
        return wrapper == null ? 1 : wrapper.getLastPage();
    }
}
