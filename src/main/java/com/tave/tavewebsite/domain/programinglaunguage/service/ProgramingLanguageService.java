package com.tave.tavewebsite.domain.programinglaunguage.service;

import com.tave.tavewebsite.domain.programinglaunguage.dto.request.LanguageLevelRequestDto;
import com.tave.tavewebsite.domain.programinglaunguage.dto.response.LanguageLevelResponseDto;
import com.tave.tavewebsite.domain.programinglaunguage.entity.LanguageLevel;
import com.tave.tavewebsite.domain.programinglaunguage.util.LanguageLevelMapper;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProgramingLanguageService {

    private final ResumeRepository resumeRepository;

    public List<LanguageLevelResponseDto> getLanguageLevel(Long resumeId) {
        Resume resume = resumeRepository.findResumeWithLanguageLevels(resumeId);

        List<LanguageLevelResponseDto> languageLevelResponseDtos = new ArrayList<>();

        for (LanguageLevel languageLevel : resume.getLanguageLevels()) {
            languageLevelResponseDtos.add(LanguageLevelMapper.toLanguageLevelResponseDto(languageLevel));
        }

        return languageLevelResponseDtos;
    }

    @Transactional
    public void patchLanguageLevel(Long id, List<LanguageLevelRequestDto> languageLevelRequestDtos) {
        Resume resumeWithLanguageLevels = resumeRepository.findResumeWithLanguageLevels(id);

        // 요청 데이터를 Map으로 변환 (language -> level)
        Map<String, Integer> levelMap = languageLevelRequestDtos.stream()
                .collect(Collectors.toMap(LanguageLevelRequestDto::language, LanguageLevelRequestDto::level));

        // 기존 LanguageLevel 리스트에서 매칭되는 경우 업데이트
        resumeWithLanguageLevels.getLanguageLevels().forEach(languageLevel -> {
            Integer newLevel = levelMap.get(languageLevel.getLanguage());
            if (newLevel != null) {
                languageLevel.patchLanguageLevel(newLevel);
            }
        });
    }

}
