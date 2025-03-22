package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.memberRepository.MemberRepository;
import com.tave.tavewebsite.domain.resume.dto.request.PersonalInfoRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.PersonalInfoResponseDto;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.exception.MemberNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
import com.tave.tavewebsite.domain.resume.mapper.ResumeMapper;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalInfoService {
    private final ResumeRepository resumeRepository;
    private final MemberRepository memberRepository;

    // 개인정보 저장
    @Transactional
    public void createPersonalInfo(Long memberId, PersonalInfoRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Resume resume = ResumeMapper.toResume(requestDto, member);
        resumeRepository.save(resume);

        // 지원 분야 값 검증 및 변환
        Resume.FieldType fieldType = validateAndConvertFieldType(requestDto.getField());

        // 개인 정보 저장
        resume.updatePersonalInfo(requestDto.getSchool(), requestDto.getMajor(),
                requestDto.getMinor(), fieldType.getMessage());
        resumeRepository.save(resume);
    }

    // 임시 저장 기능 (현재까지 입력한 정보 저장)
    @Transactional
    public void tempSavePersonalInfo(Long memberId, PersonalInfoRequestDto requestDto) {
        Resume resume = resumeRepository.findByMemberId(memberId)
                .orElseThrow(ResumeNotFoundException::new);

        // 기존 정보 갱신(임시 저장 위해)
        resume.updatePersonalInfo(requestDto.getSchool(), requestDto.getMajor(),
                requestDto.getMinor(), requestDto.getField());

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

        String field = requestDto.getField();
        Resume.FieldType fieldType = Resume.FieldType.valueOf(field.toUpperCase());

        resume.updatePersonalInfo(requestDto.getSchool(), requestDto.getMajor(),
                requestDto.getMinor(), fieldType.getMessage());
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
