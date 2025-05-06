package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.memberRepository.MemberRepository;
import com.tave.tavewebsite.domain.programinglaunguage.entity.LanguageLevel;
import com.tave.tavewebsite.domain.programinglaunguage.entity.ProgramingLanguage;
import com.tave.tavewebsite.domain.programinglaunguage.repository.LanguageLevelRepository;
import com.tave.tavewebsite.domain.programinglaunguage.repository.ProgramingLanguageRepository;
import com.tave.tavewebsite.domain.programinglaunguage.util.LanguageLevelMapper;
import com.tave.tavewebsite.domain.resume.dto.request.PersonalInfoRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.PersonalInfoResponseDto;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeQuestionResponse;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.exception.FieldTypeInvalidException;
import com.tave.tavewebsite.domain.resume.exception.MemberNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
import com.tave.tavewebsite.domain.resume.mapper.ResumeMapper;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.global.common.FieldType;
import jakarta.transaction.Transactional;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalInfoService {
    private final ResumeRepository resumeRepository;
    private final MemberRepository memberRepository;
    private final ProgramingLanguageRepository programingLanguageRepository;
    private final LanguageLevelRepository languageLevelRepository;
    private final ResumeQuestionService resumeQuestionService;

    @Transactional
    public ResumeQuestionResponse createPersonalInfo(Long memberId, PersonalInfoRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        FieldType fieldType = validateAndConvertFieldType(requestDto.getField());
        Resume savedResume = resumeRepository.save(ResumeMapper.toResume(requestDto, member, fieldType));

        List<ProgramingLanguage> byField = programingLanguageRepository.findByField(savedResume.getField());
        List<LanguageLevel> languageLevels = LanguageLevelMapper.toLanguageLevel(byField, savedResume);
        languageLevelRepository.saveAll(languageLevels);

        return resumeQuestionService.createResumeQuestion(savedResume, fieldType);
    }

    // 임시 저장 기능 (현재까지 입력한 정보 저장)
    @Transactional
    public void tempSavePersonalInfo(Long memberId, PersonalInfoRequestDto requestDto) {
        Resume resume = resumeRepository.findByMemberId(memberId)
                .orElseThrow(ResumeNotFoundException::new);

        FieldType fieldType = validateAndConvertFieldType(requestDto.getField());
        // 기존 정보 갱신 (임시 저장 위해)
        resume.updatePersonalInfo(requestDto, fieldType);
    }

    public PersonalInfoResponseDto getPersonalInfo(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);

        return ResumeMapper.toPersonalInfoResponseDto(resume);
    }

    @Transactional
    public void updatePersonalInfo(Long resumeId, PersonalInfoRequestDto requestDto) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);
        FieldType fieldType = validateAndConvertFieldType(requestDto.getField());

        resume.updatePersonalInfo(requestDto, fieldType);
    }

    @Transactional
    public void deletePersonalInfo(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);

        resumeRepository.delete(resume);
    }

    private FieldType validateAndConvertFieldType(String field) {
        try {
            return FieldType.valueOf(field.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FieldTypeInvalidException();
        }
    }

    public Resume getResumeEntityById(Long resumeId) {
        return resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);
    }

}
