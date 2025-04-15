package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.question.entity.Question;
import com.tave.tavewebsite.domain.question.service.QuestionService;
import com.tave.tavewebsite.domain.resume.dto.request.ResumeQuestionUpdateRequest;
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


@Service
@RequiredArgsConstructor
public class ResumeQuestionService {

    private final ResumeQuestionRepository resumeQuestionRepository;
    private final QuestionService questionService;

    // ResumeQuestion 생성하기
    // todo 반환 타입 Dto 설정
    public List<ResumeQuestion> createCommonResumeQuestion(Resume resume, FieldType fieldType) {
        List<Question> questionList = questionService.findQuestionsByFieldType(fieldType);
        List<ResumeQuestion> resumeQuestionList = new ArrayList<>();

        for(Question question : questionList) {
            resumeQuestionList.add(ResumeQuestion.of(resume, question));
        }

        resumeQuestionRepository.saveAll(resumeQuestionList);
        return resumeQuestionList;
    }

    // 공통 질문 수정
    @Transactional
    public void updateCommonResumeQuestion(Resume resume, List<ResumeQuestionUpdateRequest> updateRequestList) {

        List<ResumeQuestion> commonResumeQuestionList = resume.getCommonResumeQuestion();

        for(int i = 0 ; i < commonResumeQuestionList.size() ; i++) {
            ResumeQuestion target = commonResumeQuestionList.get(i);
            String answer = updateRequestList.get(i).answer();
            target.updateAnswer(answer);
        }
    }

    // 특정 분야 질문 수정
    @Transactional
    public void updateSpecificResumeQuestion(Resume resume, List<ResumeQuestionUpdateRequest> updateRequestList) {

        List<ResumeQuestion> specificResumeQuestionList = resume.getSpecificResumeQuestion();

        for(int i = 0 ; i < specificResumeQuestionList.size() ; i++) {
            ResumeQuestion target = specificResumeQuestionList.get(i);
            String answer = updateRequestList.get(i).answer();
            target.updateAnswer(answer);
        }
    }

    public List<ResumeQuestion> findResumeQuestionsByResumeId(Long resumeId) {
        List<ResumeQuestion> resumeQuestionList = resumeQuestionRepository.findByResumeId(resumeId);

        if(!resumeQuestionList.isEmpty()) {
            throw new ResumeQuestionNotMatchResumeException();
        }

        return resumeQuestionList;
    }
}
