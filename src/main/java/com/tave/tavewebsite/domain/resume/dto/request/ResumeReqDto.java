package com.tave.tavewebsite.domain.resume.dto.request;

import com.tave.tavewebsite.domain.programinglaunguage.dto.request.LanguageLevelRequestDto;
import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotReqDto;

import java.util.ArrayList;
import java.util.List;

public record ResumeReqDto(
        List<ResumeAnswerTempDto> answers,
        List<TimeSlotReqDto> timeSlots,
        List<LanguageLevelRequestDto> languageLevels
) {
    public static ResumeReqDto empty() {
        return new ResumeReqDto(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
}
