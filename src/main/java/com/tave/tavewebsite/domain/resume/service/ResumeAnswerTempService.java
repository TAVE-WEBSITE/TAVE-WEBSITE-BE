package com.tave.tavewebsite.domain.resume.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeAnswerTempDto;
import com.tave.tavewebsite.domain.resume.exception.TempParseFailedException;
import com.tave.tavewebsite.domain.resume.exception.TempNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.TempSaveFailedException;
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

    @Transactional
    public void tempSaveAnswers(Long resumeId, int page, List<ResumeAnswerTempDto> answers) {
        try {
            String json = objectMapper.writeValueAsString(answers);
            String key = "temp-resume-answer:" + resumeId + ":" + page;
            redisUtil.set(key, json, 60 * 24 * 30); // 30일 유효
        } catch (Exception e) {
            throw new TempSaveFailedException();
        }
    }

    public List<ResumeAnswerTempDto> getTempSavedAnswers(Long resumeId, int page) {
        String key = "temp-resume-answer:" + resumeId + ":" + page;
        Object data = redisUtil.get(key);
        if (data == null) {
            throw new TempNotFoundException();
        }

        try {
            return objectMapper.readValue(data.toString(), new TypeReference<>() {});
        } catch (Exception e) {
            throw new TempParseFailedException();
        }
    }
}
