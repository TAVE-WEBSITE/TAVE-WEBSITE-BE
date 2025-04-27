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
import com.tave.tavewebsite.domain.resume.entity.Resume;
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

    // 개인정보 저장
    @Transactional
    public void createPersonalInfo(Long memberId, PersonalInfoRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // 지원 분야 값 검증 및 변환
        Resume.FieldType fieldType = validateAndConvertFieldType(requestDto.getField());
        Resume savedResume = resumeRepository.save(ResumeMapper.toResume(requestDto, member));

        // 필드값 기준으로 질문들을 찾은 후 해당 질문들을 모두 저장시키는 과정
        List<ProgramingLanguage> byField = programingLanguageRepository.findByField(
                FieldType.valueOf(
                        savedResume.getField())); //todo *****지우야**** 여기 지금 resume field가 String으로 돼있어서 내가 강제로 바꿔놨는데 field 타입 바꾸고 나서 여기 코드도 수정해줘
        List<LanguageLevel> languageLevel = LanguageLevelMapper.toLanguageLevel(byField, savedResume);

        languageLevelRepository.saveAll(languageLevel);
    }

    // 임시 저장 기능 (현재까지 입력한 정보 저장)
    @Transactional
    public void tempSavePersonalInfo(Long memberId, PersonalInfoRequestDto requestDto) {
        Resume resume = resumeRepository.findByMemberId(memberId)
                .orElseThrow(ResumeNotFoundException::new);

        // 기존 정보 갱신 (임시 저장 위해)
        resume.updatePersonalInfo(requestDto);
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

        // 지원 분야 값 검증 및 변환
        Resume.FieldType fieldType = validateAndConvertFieldType(requestDto.getField());

        resume.updatePersonalInfo(requestDto);
    }

    @Transactional
    public void deletePersonalInfo(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);

        resumeRepository.delete(resume);
    }

    // 지원 분야 값 검증 및 변환 메서드
    private Resume.FieldType validateAndConvertFieldType(String field) {
        try {
            return Resume.FieldType.valueOf(field.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResumeNotFoundException();
        }
    }
}
