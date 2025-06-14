package com.tave.tavewebsite.domain.resume.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.tavewebsite.domain.applicant.history.entity.ApplicantHistory;
import com.tave.tavewebsite.domain.applicant.history.entity.ApplicationStatus;
import com.tave.tavewebsite.domain.applicant.history.repository.ApplicantHistoryRepository;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.memberRepository.MemberRepository;
import com.tave.tavewebsite.domain.programinglaunguage.entity.LanguageLevel;
import com.tave.tavewebsite.domain.programinglaunguage.entity.ProgramingLanguage;
import com.tave.tavewebsite.domain.programinglaunguage.repository.LanguageLevelRepository;
import com.tave.tavewebsite.domain.programinglaunguage.repository.ProgramingLanguageRepository;
import com.tave.tavewebsite.domain.programinglaunguage.util.LanguageLevelMapper;
import com.tave.tavewebsite.domain.resume.dto.request.PersonalInfoCreateRequestDto;
import com.tave.tavewebsite.domain.resume.dto.request.PersonalInfoRequestDto;
import com.tave.tavewebsite.domain.resume.dto.request.TempPersonalInfoDto;
import com.tave.tavewebsite.domain.resume.dto.response.PersonalInfoResponseDto;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeQuestionResponse;
import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotResDto;
import com.tave.tavewebsite.domain.resume.entity.InterviewTime;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.exception.FieldTypeInvalidException;
import com.tave.tavewebsite.domain.resume.exception.MemberNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.TempNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.TempParseFailedException;
import com.tave.tavewebsite.domain.resume.exception.TempSerializeFailedException;
import com.tave.tavewebsite.domain.resume.mapper.ResumeMapper;
import com.tave.tavewebsite.domain.resume.repository.InterviewTimeRepository;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.global.common.FieldType;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
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
    private final InterviewTimeRepository interviewTimeRepository;
    private final ApplicantHistoryRepository applicantHistoryRepository;

    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;

    @Transactional
    public Resume createPersonalInfo(Long memberId, PersonalInfoCreateRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        FieldType fieldType = validateAndConvertFieldType(requestDto.getField());
        Resume savedResume = resumeRepository.save(ResumeMapper.toResume(requestDto, member, fieldType));

        createApplicantHistory(fieldType, member, requestDto.getGeneration());
        List<ProgramingLanguage> byField = programingLanguageRepository.findByField(savedResume.getField());
        List<LanguageLevel> languageLevels = LanguageLevelMapper.toLanguageLevel(byField, savedResume);
        languageLevelRepository.saveAll(languageLevels);

        return savedResume;
    }

    public ResumeQuestionResponse createResumeQuestions(Resume resume) {
        return resumeQuestionService.createResumeQuestion(resume, resume.getField());
    }


    // 임시 저장 기능 (현재까지 입력한 정보 저장)
    @Transactional
    public void tempSavePersonalInfo(Long memberId, PersonalInfoRequestDto requestDto) {
        TempPersonalInfoDto tempDto = new TempPersonalInfoDto(
                requestDto.getSchool(),
                requestDto.getMajor(),
                requestDto.getMinor(),
                requestDto.getField()
        );

        try {
            String json = objectMapper.writeValueAsString(tempDto);
            String key = "temp-resume:" + memberId;
            redisUtil.set(key, json, 60 * 24 * 30);
        } catch (Exception e) {
            throw new TempSerializeFailedException();
        }
    }

    // Redis에서 임시저장 불러오기
    public TempPersonalInfoDto getTempSavedPersonalInfo(Long memberId) {
        String key = "temp-resume:" + memberId;
        Object data = redisUtil.get(key);
        if (data == null) {
            throw new TempNotFoundException();
        }

        try {
            return objectMapper.readValue(data.toString(), TempPersonalInfoDto.class);
        } catch (Exception e) {
            throw new TempParseFailedException();
        }
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

    public PersonalInfoResponseDto getAllPersonalInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Resume resume = resumeRepository.findByMemberId(memberId)
                .orElse(null); // 없을 수도 있음

        return PersonalInfoResponseDto.builder()
                .username(member.getUsername())
                .sex(member.getSex().name())
                .birthday(member.getBirthday().toString())
                .phoneNumber(member.getPhoneNumber())
                .email(member.getEmail())
                .school(resume != null ? resume.getSchool() : null)
                .major(resume != null ? resume.getMajor() : null)
                .minor(resume != null ? resume.getMinor() : null)
                .field(resume != null ? resume.getField().name() : null)
                .build();
    }

    @Transactional
    public void updatePersonalInfoByMemberId(Long memberId, PersonalInfoRequestDto requestDto) {
        FieldType fieldType = validateAndConvertFieldType(requestDto.getField());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        resumeRepository.findByMemberId(memberId)
                .ifPresent(resume -> resume.updatePersonalInfo(requestDto, fieldType));
    }

    public void submitResume(Long resumeId) {
        Resume resume = getResumeEntityById(resumeId);
        resume.submit();
        resumeRepository.save(resume);
    }

    public List<TimeSlotResDto> getInterviewTime() {
        List<InterviewTime> all = interviewTimeRepository.findAll();

        return all.stream().map(
                TimeSlotResDto::fromInterviewTime
        ).toList();
    }

    private void createApplicantHistory(FieldType fieldType, Member member, String generation) {
        ApplicantHistory applicantHistory = new ApplicantHistory(generation, fieldType, ApplicationStatus.DRAFT,
                member);
        applicantHistoryRepository.save(applicantHistory);
    }

}
