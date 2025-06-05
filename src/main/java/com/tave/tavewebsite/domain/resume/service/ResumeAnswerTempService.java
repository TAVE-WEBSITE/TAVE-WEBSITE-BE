package com.tave.tavewebsite.domain.resume.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeAnswerTempWrapper;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeReqDto;
import com.tave.tavewebsite.domain.resume.exception.TempNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.TempParseFailedException;
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

    @Transactional
    public void tempSaveAnswers(Long resumeId, int page, ResumeReqDto tempDto) {
        try {
            ResumeAnswerTempWrapper wrapper = new ResumeAnswerTempWrapper(page, tempDto.answers(), tempDto.timeSlots());
            String json = objectMapper.writeValueAsString(wrapper);
            String key = "temp-resume-answer-" + page + ":" + resumeId;
            redisUtil.set(key, json, 60 * 24 * 30); // 30일 유효
        } catch (Exception e) {
            throw new TempSaveFailedException();
        }
    }

    public ResumeAnswerTempWrapper getTempSavedAnswers(int page, Long resumeId) {
        String key = "temp-resume-answer-" + page + ":" + resumeId;
        Object data = redisUtil.get(key);
        if (data == null) {
            throw new TempNotFoundException();
        }

        try {
            return objectMapper.readValue(data.toString(), ResumeAnswerTempWrapper.class);
        } catch (Exception e) {
            throw new TempParseFailedException();
        }
    }
}
