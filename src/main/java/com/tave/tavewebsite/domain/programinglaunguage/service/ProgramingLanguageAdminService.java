package com.tave.tavewebsite.domain.programinglaunguage.service;

import com.tave.tavewebsite.domain.programinglaunguage.dto.request.ProgramingLanguageRequestDto;
import com.tave.tavewebsite.domain.programinglaunguage.dto.response.ProgrammingLanguageResponseDto;
import com.tave.tavewebsite.domain.programinglaunguage.entity.ProgramingLanguage;
import com.tave.tavewebsite.domain.programinglaunguage.exception.LanguageErrorException;
import com.tave.tavewebsite.domain.programinglaunguage.repository.ProgramingLanguageRepository;
import com.tave.tavewebsite.domain.programinglaunguage.util.LanguageLevelMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProgramingLanguageAdminService {

    private final ProgramingLanguageRepository programingLanguageRepository;

    public List<ProgrammingLanguageResponseDto> getAllProgramingLanguagesByField(
            String field) {
        List<ProgramingLanguage> programingLanguagesByField = programingLanguageRepository.findByField(
                LanguageLevelMapper.validateAndConvertFieldType(field));
        return LanguageLevelMapper.toProgrammingLanguageResponseDtoList(programingLanguagesByField);
    }

    @Transactional
    public void createProgramingLanguage(ProgramingLanguageRequestDto programingLanguageRequestDto) {
        programingLanguageRepository.save(LanguageLevelMapper.toProgramingLanguage(programingLanguageRequestDto));
    }

    @Transactional
    public void deleteProgramingLanguage(Long id) {
        programingLanguageRepository.findById(id)
                .orElseThrow(LanguageErrorException.NotFoundLanguageException::new);
        programingLanguageRepository.deleteById(id);
    }

}
