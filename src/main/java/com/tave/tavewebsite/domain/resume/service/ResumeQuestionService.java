package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.question.entity.Question;
import com.tave.tavewebsite.domain.question.service.QuestionService;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeQuestion;
import com.tave.tavewebsite.domain.resume.repository.ResumeQuestionRepository;
import com.tave.tavewebsite.global.common.FieldType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class ResumeQuestionService {

    ResumeQuestionRepository resumeQuestionRepository;
    QuestionService questionService;

    /*
    * 1. 특정 분야 질문 DB에서 가져오기, 공통 분야 질문 DB에서 가져오기
    * 2-1. 특정 분야 질문 내용(Question.content) 가져오기
    * 2-2. 공통 분야 질문 내용(Question.content) 가져오기
    * 3. 해당 분야 질문 만큼ResumeQuestion 엔티티를 생성
    * 3-1. 특정 분야 질문 수 만큼 ResumeQuestion 생성
    * 3-2. 공통 분야 질문 수 만큼 ResumeQuestion 생성
    * 4. 생성한 ResumeQuesiton을 Resume과 연관지음
    * */

    // ResumeQuestion 생성하기
    public List<ResumeQuestion> createCommonResumeQuestion(Resume resume, FieldType fieldType) {
        List<Question> questionList = questionService.findQuestionsByFieldType(fieldType);
        List<ResumeQuestion> resumeQuestionList = new ArrayList<>();

        for(Question q: questionList) {
            resumeQuestionList.add(ResumeQuestion.of(resume, q, fieldType));
        }

        return resumeQuestionList;
    }
}
