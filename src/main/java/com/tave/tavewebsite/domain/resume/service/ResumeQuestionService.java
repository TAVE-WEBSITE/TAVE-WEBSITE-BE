package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.question.entity.Question;
import com.tave.tavewebsite.domain.question.service.QuestionService;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeQuestionUpdateRequest;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeQuestionResponse;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeQuestion;
import com.tave.tavewebsite.domain.resume.exception.ResumeQuestionNotMatchResumeException;
import com.tave.tavewebsite.domain.resume.repository.ResumeQuestionRepository;
import com.tave.tavewebsite.global.common.FieldType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeQuestionService {

    private final ResumeQuestionRepository resumeQuestionRepository;
    private final QuestionService questionService;

    // ResumeQuestion 생성하기
    // todo 반환 타입 Dto 설정
    public List<ResumeQuestionResponse> createResumeQuestion(Resume resume, FieldType fieldType) {
        List<Question> questionList = questionService.findQuestionsByFieldType(fieldType);
        List<ResumeQuestion> resumeQuestionList = new ArrayList<>();

        for(Question question : questionList) {
            resumeQuestionList.add(ResumeQuestion.of(resume, question));
        }

        resumeQuestionRepository.saveAll(resumeQuestionList);
        return mapResumeQuestionListToResponse(resumeQuestionList);
    }

    // 분야 별 ResumeQuestion 조회하기
    public List<ResumeQuestionResponse> getResumeQuestionList(Resume resume, FieldType fieldType) {

        List<ResumeQuestion> resumeQuestionList =
                findResumeQuestionsByResumeId(resume, fieldType);

        return mapResumeQuestionListToResponse(resumeQuestionList);
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

        log.info("조회 테스트 list 사이즈: " + resumeQuestionList.size());

        if(resumeQuestionList.isEmpty()) {
            throw new ResumeQuestionNotMatchResumeException();
        }

        return resumeQuestionList;
    }

    public List<ResumeQuestionResponse> mapResumeQuestionListToResponse(List<ResumeQuestion> resumeQuestionList) {
        return resumeQuestionList
                .stream()
                .map(ResumeQuestionResponse::from)
                .toList();
    }
}
