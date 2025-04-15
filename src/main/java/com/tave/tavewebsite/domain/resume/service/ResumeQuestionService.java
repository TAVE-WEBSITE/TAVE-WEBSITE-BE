package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.question.entity.Question;
import com.tave.tavewebsite.domain.question.service.QuestionService;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeQuestionUpdateRequest;
import com.tave.tavewebsite.domain.resume.dto.response.DetailResumeQuestionResponse;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeQuestionResponse;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeQuestion;
import com.tave.tavewebsite.domain.resume.exception.ResumeQuestionNotMatchResumeException;
import com.tave.tavewebsite.domain.resume.repository.ResumeQuestionJdbcRepository;
import com.tave.tavewebsite.domain.resume.repository.ResumeQuestionRepository;
import com.tave.tavewebsite.global.common.FieldType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeQuestionService {

    private final ResumeQuestionRepository resumeQuestionRepository;
    private final ResumeQuestionJdbcRepository resumeQuestionJdbcRepository;
    private final QuestionService questionService;

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
        List<ResumeQuestion> resumeQuestionList = findResumeQuestionsByResumeId(resume, fieldType);

        return mapResumeQuestionListToDetailResponse(resumeQuestionList);
    }

    // 공통 질문 수정
    @Transactional
    public void updateCommonResumeQuestion(Resume resume, List<ResumeQuestionUpdateRequest> updateRequestList, FieldType fieldType) {

        List<ResumeQuestion> resumeQuestionList = findResumeQuestionsByResumeId(resume, fieldType);

        for(int i = 0 ; i < resumeQuestionList.size() ; i++) {
            ResumeQuestion target = resumeQuestionList.get(i);
            String answer = updateRequestList.get(i).answer();
            target.updateAnswer(answer);
        }
    }

    /*
    * refactor
    * */

    // Reusme + FieldType으로 조회
    public List<ResumeQuestion> findResumeQuestionsByResumeId(Resume resume, FieldType fieldType) {
        List<ResumeQuestion> resumeQuestionList = resumeQuestionRepository.findByResumeAndFieldType(resume, fieldType);

        if(resumeQuestionList.isEmpty()) {
            throw new ResumeQuestionNotMatchResumeException();
        }

        return resumeQuestionList;
    }

    public List<DetailResumeQuestionResponse> mapResumeQuestionListToDetailResponse(List<ResumeQuestion> resumeQuestionList) {
        return resumeQuestionList
                .stream()
                .map(DetailResumeQuestionResponse::from)
                .toList();
    }

    private List<ResumeQuestion> createResumeQuestions(Resume resume, FieldType fieldType) {
        return questionService.findQuestionsByFieldType(fieldType).stream()
                .map(q -> ResumeQuestion.of(resume, q))
                .toList();
    }

    private List<ResumeQuestion> concatResumeQuestion(List<ResumeQuestion> commonQuestionList, List<ResumeQuestion> specificQuestionList) {
        return Stream.concat(commonQuestionList.stream(), specificQuestionList.stream())
                .toList();
    }
}
