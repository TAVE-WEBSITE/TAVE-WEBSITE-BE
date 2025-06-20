package com.tave.tavewebsite.domain.resume.dto.request;

import com.tave.tavewebsite.domain.programinglaunguage.dto.response.LanguageLevelResponseDto;
import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotReqDto;
import java.util.List;

public record ResumeReqDto(
        List<ResumeAnswerTempDto> answers,
        List<TimeSlotReqDto> timeSlots,
        List<LanguageLevelResponseDto> languageLevels,
        String githubUrl,
        String blogUrl,
        String portfolioUrl
) {
}
