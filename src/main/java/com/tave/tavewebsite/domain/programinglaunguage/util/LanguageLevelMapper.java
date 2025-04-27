package com.tave.tavewebsite.domain.programinglaunguage.util;

import com.tave.tavewebsite.domain.programinglaunguage.dto.request.ProgramingLanguageRequestDto;
import com.tave.tavewebsite.domain.programinglaunguage.dto.response.LanguageLevelResponseDto;
import com.tave.tavewebsite.domain.programinglaunguage.dto.response.ProgrammingLanguageResponseDto;
import com.tave.tavewebsite.domain.programinglaunguage.entity.LanguageLevel;
import com.tave.tavewebsite.domain.programinglaunguage.entity.ProgramingLanguage;
import com.tave.tavewebsite.domain.programinglaunguage.exception.LanguageErrorException.NotFoundFieldException;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.global.common.FieldType;
import java.util.ArrayList;
import java.util.List;

public class LanguageLevelMapper {

    public static LanguageLevelResponseDto toLanguageLevelResponseDto(LanguageLevel languageLevel) {
        return new LanguageLevelResponseDto(languageLevel.getLanguage(), languageLevel.getLevel());
    }

    public static List<LanguageLevel> toLanguageLevel(List<ProgramingLanguage> programingLanguages, Resume resume) {
        List<LanguageLevel> languageLevels = new ArrayList<>();
        for (ProgramingLanguage programingLanguage : programingLanguages) {
            languageLevels.add(
                    LanguageLevel.builder().language(programingLanguage.getLanguage()).resume(resume).build());
        }

        return languageLevels;
    }

    public static ProgramingLanguage toProgramingLanguage(ProgramingLanguageRequestDto programingLanguageRequestDto) {
        FieldType fieldType = validateAndConvertFieldType(programingLanguageRequestDto.field());
        return ProgramingLanguage.builder().field(fieldType).language(
                programingLanguageRequestDto.language()).build();
    }

    public static List<ProgrammingLanguageResponseDto> toProgrammingLanguageResponseDtoList(
            List<ProgramingLanguage> programingLanguages) {
        List<ProgrammingLanguageResponseDto> programmingLanguageResponseDtoList = new ArrayList<>();
        for (ProgramingLanguage programingLanguage : programingLanguages) {
            programmingLanguageResponseDtoList.add(
                    new ProgrammingLanguageResponseDto(programingLanguage.getId(), programingLanguage.getField().name(),
                            programingLanguage.getLanguage())
            );
        }
        return programmingLanguageResponseDtoList;
    }

    public static FieldType validateAndConvertFieldType(String field) {
        try {
            return FieldType.valueOf(field.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotFoundFieldException();
        }
    }
}
