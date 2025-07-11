package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.applicant.history.entity.ApplicantHistory;
import com.tave.tavewebsite.domain.applicant.history.entity.ApplicationStatus;
import com.tave.tavewebsite.domain.applicant.history.repository.ApplicantHistoryRepository;
import com.tave.tavewebsite.domain.applicant.history.service.ApplicantHistoryService;
import com.tave.tavewebsite.domain.apply.dashboard.service.DashboardService;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.memberRepository.MemberRepository;
import com.tave.tavewebsite.domain.programinglaunguage.entity.LanguageLevel;
import com.tave.tavewebsite.domain.programinglaunguage.entity.ProgramingLanguage;
import com.tave.tavewebsite.domain.programinglaunguage.repository.LanguageLevelRepository;
import com.tave.tavewebsite.domain.programinglaunguage.repository.ProgramingLanguageRepository;
import com.tave.tavewebsite.domain.programinglaunguage.util.LanguageLevelMapper;
import com.tave.tavewebsite.domain.resume.controller.PersonalInfoSuccessMessage;
import com.tave.tavewebsite.domain.resume.dto.request.PersonalInfoCreateRequestDto;
import com.tave.tavewebsite.domain.resume.dto.request.PersonalInfoRequestDto;
import com.tave.tavewebsite.domain.resume.dto.response.CreatePersonalInfoResponse;
import com.tave.tavewebsite.domain.resume.dto.response.DetailResumeQuestionResponse;
import com.tave.tavewebsite.domain.resume.dto.response.PersonalInfoResponseDto;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeQuestionResponse;
import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotResDto;
import com.tave.tavewebsite.domain.resume.entity.InterviewTime;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeQuestion;
import com.tave.tavewebsite.domain.resume.exception.FieldTypeInvalidException;
import com.tave.tavewebsite.domain.resume.exception.MemberNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
import com.tave.tavewebsite.domain.resume.mapper.ResumeMapper;
import com.tave.tavewebsite.domain.resume.repository.InterviewTimeRepository;
import com.tave.tavewebsite.domain.resume.repository.ResumeQuestionRepository;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.global.common.FieldType;
import com.tave.tavewebsite.global.redis.utils.RedisUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private final ApplicantHistoryService applicantHistoryService;
    private final ResumeQuestionRepository resumeQuestionRepository;
    private final DashboardService dashboardService;

    private final RedisUtil redisUtil;

    @Transactional
    public CreatePersonalInfoResponse createPersonalInfoAndQuestions(Long memberId,
                                                                     PersonalInfoCreateRequestDto requestDto) {
        Resume resume = createPersonalInfo(memberId, requestDto);
        ResumeQuestionResponse questions = createResumeQuestions(resume);

        return CreatePersonalInfoResponse.of(
                PersonalInfoSuccessMessage.CREATE_SUCCESS.getMessage(),
                questions,
                resume.getId()
        );
    }

    @Transactional
    public Resume createPersonalInfo(Long memberId, PersonalInfoCreateRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        FieldType fieldType = validateAndConvertFieldType(requestDto.getField());

        // 기존 이력서 조회
        Resume existingResume = resumeRepository.findByMemberId(memberId).orElse(null);

        if (existingResume != null) {
            FieldType existingFieldType = existingResume.getField();

            if (!existingFieldType.equals(fieldType)) {
                // FieldType 이 변경된 경우 기존 이력서 및 관련 데이터 제거
                deleteRelatedResumeData(existingResume);

                // 새로운 이력서 생성
                Resume newResume = resumeRepository.save(ResumeMapper.toResume(requestDto, member, fieldType));

                // 마이페이지 지원 fieldType 변경
                applicantHistoryService.changeApplicantFieldType(fieldType, memberId, requestDto.getGeneration());

                createLanguages(newResume);
                return newResume;
            }

            // FieldType 같으면 기존 Resume 정보만 업데이트
            PersonalInfoRequestDto requestAsUpdateDto = PersonalInfoRequestDto.fromCreateRequest(requestDto);
            existingResume.updatePersonalInfo(requestAsUpdateDto, fieldType);
            return resumeRepository.save(existingResume);
        }

        // 없으면 새로 생성
        Resume savedResume = resumeRepository.save(ResumeMapper.toResume(requestDto, member, fieldType));

        createApplicantHistory(fieldType, member, requestDto.getGeneration());
        List<ProgramingLanguage> byField = programingLanguageRepository.findByField(savedResume.getField());
        List<LanguageLevel> languageLevels = LanguageLevelMapper.toLanguageLevel(byField, savedResume);
        languageLevelRepository.saveAll(languageLevels);

        return savedResume;
    }

    public ResumeQuestionResponse createResumeQuestions(Resume resume) {
        boolean exists = resumeQuestionRepository.existsByResumeId(resume.getId());
        if (exists) {
            // 이미 질문이 있으면 기존 질문 불러오기
            List<ResumeQuestion> questions = resumeQuestionRepository.findByResumeId(resume.getId());
            return ResumeQuestionResponse.of(
                    questions.stream()
                            .filter(q -> q.getFieldType() == FieldType.COMMON)
                            .map(DetailResumeQuestionResponse::from)
                            .toList(),
                    questions.stream()
                            .filter(q -> q.getFieldType() != FieldType.COMMON)
                            .map(DetailResumeQuestionResponse::from)
                            .toList()
            );
        } else {
            // 질문이 없으면 새로 생성
            return resumeQuestionService.createResumeQuestion(resume, resume.getField());
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
        dashboardService.addDetailedCount(resume, resume.getMember());
    }

    public List<TimeSlotResDto> getInterviewTime() {
        List<InterviewTime> all = interviewTimeRepository.findAll();

        return all.stream().map(
                TimeSlotResDto::fromInterviewTime
        ).toList();
    }

    private void createApplicantHistory(FieldType fieldType, Member member, String generation) {
        ApplicantHistory applicantHistory =
                new ApplicantHistory(generation, fieldType, ApplicationStatus.DRAFT, member);
        applicantHistoryRepository.save(applicantHistory);
    }

    private void deleteRelatedResumeData(Resume resume) {
        Long resumeId = resume.getId();

        // 1. Redis 임시저장 삭제
        redisUtil.deleteByPrefix("resume:" + resumeId + ":");
        // 2. 프로그래밍 언어 레벨 삭제
        languageLevelRepository.deleteByResumeId(resumeId);
        // 3. 질문 삭제
        resumeQuestionRepository.deleteByResumeId(resumeId);
        // 4. 이력서 삭제
        resumeRepository.delete(resume);
    }

    private void createLanguages(Resume resume) {
        List<ProgramingLanguage> byField = programingLanguageRepository.findByField(resume.getField());
        List<LanguageLevel> languageLevels = LanguageLevelMapper.toLanguageLevel(byField, resume);
        languageLevelRepository.saveAll(languageLevels);
    }
}
