package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.memberRepository.MemberRepository;
import com.tave.tavewebsite.domain.resume.dto.request.PersonalInfoRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.PersonalInfoResponseDto;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.exception.MemberNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
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

        Resume resume = Resume.builder()
                .member(member)
                .school(requestDto.getSchool())
                .major(requestDto.getMajor())
                .minor(requestDto.getMinor())
                .field(requestDto.getField())
                .build();

        // 지원 분야 값 검증
        String field = requestDto.getField();
        Resume.FieldType fieldType;
        try{
            fieldType = Resume.FieldType.valueOf(field.toUpperCase());
        } catch (IllegalArgumentException e){
            throw new ResumeNotFoundException();
        }

        // 개인 정보 저장
        resume.updatePersonalInfo(requestDto.getSchool(), requestDto.getMajor(),
                requestDto.getMinor(), fieldType.getMessage());
        resumeRepository.save(resume);
    }

    public PersonalInfoResponseDto getPersonalInfo(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(ResumeNotFoundException::new);

        return new PersonalInfoResponseDto(resume);
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

}
