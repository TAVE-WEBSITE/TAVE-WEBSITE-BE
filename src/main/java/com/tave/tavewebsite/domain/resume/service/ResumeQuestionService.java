package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.programinglaunguage.dto.response.LanguageLevelResponseDto;
import com.tave.tavewebsite.domain.programinglaunguage.service.ProgramingLanguageService;
import com.tave.tavewebsite.domain.question.service.QuestionService;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeQuestionUpdateRequest;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeReqDto;
import com.tave.tavewebsite.domain.resume.dto.response.*;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeQuestion;
import com.tave.tavewebsite.domain.resume.exception.AnswerTextLengthOverException;
import com.tave.tavewebsite.domain.resume.exception.InvalidPageNumberException;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.ResumeQuestionNotMatchResumeException;
import com.tave.tavewebsite.domain.resume.repository.ResumeQuestionJdbcRepository;
import com.tave.tavewebsite.domain.resume.repository.ResumeQuestionRepository;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.global.common.FieldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeQuestionService {

    private final ResumeRepository resumeRepository;
    private final ResumeQuestionRepository resumeQuestionRepository;
    private final ResumeQuestionJdbcRepository resumeQuestionJdbcRepository;
    private final QuestionService questionService;
    private final ResumeTimeService resumeTimeService;
    private final ProgramingLanguageService programingLanguageService;

    public ResumeQuestionResponse createResumeQuestion(Resume resume, FieldType fieldType) {
        // 공통 + 전공별 질문 생성
        List<ResumeQuestion> commonQuestions = createResumeQuestions(resume, FieldType.COMMON);
        List<ResumeQuestion> specificQuestions = createResumeQuestions(resume, fieldType);

        // Bulk Insert
        List<ResumeQuestion> allResumeQuestions = concatResumeQuestion(commonQuestions, specificQuestions);
        resumeQuestionJdbcRepository.bulkInsert(allResumeQuestions, resume);

        // 응답 변환
        return ResumeQuestionResponse.of(
                mapResumeQuestionListToDetailResponse(commonQuestions),
                mapResumeQuestionListToDetailResponse(specificQuestions)
        );
    }

    // 분야 별 ResumeQuestion 조회하기
    public List<DetailResumeQuestionResponse> getResumeQuestionList(Resume resume, FieldType fieldType) {
        List<ResumeQuestion> resumeQuestionList = findResumeQuestionsByResumeId(resume, fieldType)
                .stream()
                .sorted(Comparator.comparing(ResumeQuestion::getOrdered))
                .toList();

        return mapResumeQuestionListToDetailResponse(resumeQuestionList);
    }

    // 공통 질문 수정
    @Transactional
    public void updateCommonResumeQuestion(Resume resume, List<ResumeQuestionUpdateRequest> updateRequestList,
                                           FieldType fieldType) {

        List<ResumeQuestion> resumeQuestionList = findResumeQuestionsByResumeId(resume, fieldType);

        for (int i = 0; i < resumeQuestionList.size(); i++) {
            ResumeQuestion target = resumeQuestionList.get(i);
            String answer = updateRequestList.get(i).answer();
            if (target.getTextLength() < answer.length()) {
                throw new AnswerTextLengthOverException();
            }
            target.updateAnswer(answer);
        }
    }

    /*
     * refactor
     * */

    // Reusme + FieldType으로 조회
    public List<ResumeQuestion> findResumeQuestionsByResumeId(Resume resume, FieldType fieldType) {
        List<ResumeQuestion> resumeQuestionList = resumeQuestionRepository.findByResumeAndFieldType(resume, fieldType);

        if (resumeQuestionList.isEmpty()) {
            throw new ResumeQuestionNotMatchResumeException();
        }

        return resumeQuestionList;
    }

    public List<DetailResumeQuestionResponse> mapResumeQuestionListToDetailResponse(
            List<ResumeQuestion> resumeQuestionList) {
        return resumeQuestionList
                .stream()
                .map(DetailResumeQuestionResponse::from)
                .toList();
    }

    @Transactional
    public void createResumeAnswer(Long resumeId, ResumeReqDto reqDto) {
        Resume resume = findIfResumeExists(resumeId);

        // resume와 resumeQuestion이 매핑되어 있는지 유효성 검증을 해야하는가?
        reqDto.answers().forEach(answer -> {
            ResumeQuestion resumeQuestion = findIfResumeQuestionExists(answer.getResumeQuestionId());
            resumeQuestion.updateAnswer(answer.getAnswer());
        });

        if (!reqDto.languageLevels().isEmpty()) {
            programingLanguageService.patchLanguageLevel(resumeId, reqDto.languageLevels());
        }

        if (!reqDto.timeSlots().isEmpty()) {
            resumeTimeService.createTimeSlot(resumeId, reqDto.timeSlots());
            resume.submit();
        }
    }

    @Transactional(readOnly = true)
    public List<DetailResumeQuestionResponse> getResumeQuestionPage(Resume resume, int page) {
        FieldType targetFieldType;

        if (page == 1) {
            targetFieldType = resume.getField(); // 사용자가 지원한 분야
        } else if (page == 2) {
            targetFieldType = FieldType.COMMON; // 공통
        } else {
            throw new InvalidPageNumberException();
        }

        return getResumeQuestionList(resume, targetFieldType);
    }

    private List<ResumeQuestion> createResumeQuestions(Resume resume, FieldType fieldType) {
        return questionService.findQuestionsByFieldType(fieldType).stream()
                .map(q -> ResumeQuestion.of(resume, q))
                .toList();
    }

    private List<ResumeQuestion> concatResumeQuestion(List<ResumeQuestion> commonQuestionList,
                                                      List<ResumeQuestion> specificQuestionList) {
        return Stream.concat(commonQuestionList.stream(), specificQuestionList.stream())
                .toList();
    }

    private Resume findIfResumeExists(Long resumeId) {
        return resumeRepository.findById(resumeId).orElseThrow(ResumeNotFoundException::new);
    }

    private ResumeQuestion findIfResumeQuestionExists(Long resumeQuestionId) {
        return resumeQuestionRepository.findById(resumeQuestionId)
                .orElseThrow(ResumeQuestionNotMatchResumeException::new);
    }

//    @Transactional(readOnly = true)
//    public ResumeListResponse getResumeListDetails(List<Long> resumeIds) {
//        List<Resume> resumes = resumeRepository.findAllWithTimeSlotsByIdIn(resumeIds);
//
//        List<ResumeResponse> resumeResponseList = new ArrayList<>();
//
//        for (Resume resume : resumes) {
//            log.info("resume id: {} COMMON 조회 시작 ", resume.getId());
//            List<DetailResumeQuestionResponse> commonQuestions = getResumeQuestionList(resume, FieldType.COMMON);
//            log.info("resume id: {} 파트별 조회 시작 ", resume.getId());
//            List<DetailResumeQuestionResponse> specificQuestions = getResumeQuestionList(resume, resume.getField());
//
//            List<CommonResumeResponse> commonList = List.of(CommonResumeResponse.of(resume, commonQuestions));
//
//            List<LanguageLevelResponseDto> languageLevels = resume.getProgramingLanguages().stream()
//                    .map(LanguageLevelResponseDto::fromEntity)
//                    .toList();
//
//            List<SpecificResumeResponseDto> specificList = List.of(SpecificResumeResponseDto.of(
//                    resume.getId(),
//                    specificQuestions,
//                    languageLevels
//            ));
//
//            resumeResponseList.add(ResumeResponse.of(resume.getId(), commonList, specificList));
//        }
//
//        return ResumeListResponse.of(resumeResponseList);
//    }

    @Transactional(readOnly = true)
    public ResumeListResponse getResumeListDetails(List<Long> resumeIds) {
//        List<Resume> resumes = resumeRepository.findAllWithTimeSlotsByIdIn(resumeIds);
        List<Resume> resumes = resumeRepository.findAllByMemberIdWithTimeSlotsIn(resumeIds);

        List<ResumeResponse> resumeResponseList = new ArrayList<>();

        for (Resume resume : resumes) {
            List<DetailResumeQuestionResponse> commonQuestions = new ArrayList<>();
            List<DetailResumeQuestionResponse> specificQuestions = new ArrayList<>();
            List<ResumeQuestion> rqList = resume.getResumeQuestions();

            rqList.forEach(rq -> {
                DetailResumeQuestionResponse response = DetailResumeQuestionResponse.from(rq);
                if (rq.getFieldType() == FieldType.COMMON) {
                    commonQuestions.add(response);
                } else {
                    specificQuestions.add(response);
                }
            });

            List<CommonResumeResponse> commonList = List.of(CommonResumeResponse.of(resume, commonQuestions));

            List<LanguageLevelResponseDto> languageLevels = resume.getProgramingLanguages().stream()
                    .map(LanguageLevelResponseDto::fromEntity)
                    .toList();

            List<SpecificResumeResponseDto> specificList = List.of(SpecificResumeResponseDto.of(
                    resume.getId(),
                    specificQuestions,
                    languageLevels
            ));

            ResumeMemberInfoDto resumeMemberInfoDto = ResumeMemberInfoDto.of(resume);

            resumeResponseList.add(ResumeResponse.of(resume.getId(), commonList, specificList, resumeMemberInfoDto));
        }

        return ResumeListResponse.of(resumeResponseList);
    }

    public List<ResumeQuestion> getResumeListByResumeId(Long resumeId) {
        return resumeQuestionRepository.findByResumeId(resumeId);
    }

    @Transactional(readOnly = true)
    public ResumeDetailResponse getResumeDetail(Long resumeId) {
        Resume resume = resumeRepository.findWithTimeSlotsById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException());

        List<DetailResumeQuestionResponse> commonQuestions = new ArrayList<>();
        List<DetailResumeQuestionResponse> specificQuestions = new ArrayList<>();

        for (ResumeQuestion rq : resume.getResumeQuestions()) {
            DetailResumeQuestionResponse dto = DetailResumeQuestionResponse.from(rq);
            if (rq.getFieldType() == FieldType.COMMON) {
                commonQuestions.add(dto);
            } else {
                specificQuestions.add(dto);
            }
        }

        List<LanguageLevelResponseDto> languageLevels = resume.getProgramingLanguages().stream()
                .map(LanguageLevelResponseDto::fromEntity)
                .toList();

        List<DetailResumeQuestionResponse> commonDtoList = commonQuestions
                .stream()
                .sorted(Comparator.comparing(DetailResumeQuestionResponse::ordered))
                .toList();

        List<DetailResumeQuestionResponse> specificDtoList = specificQuestions
                .stream()
                .sorted(Comparator.comparing(DetailResumeQuestionResponse::ordered))
                .toList();

        return ResumeDetailResponse.of(resume, commonQuestions, specificQuestions, languageLevels);
    }

}
