package com.tave.tavewebsite.domain.question.service;

import com.tave.tavewebsite.domain.question.dto.request.QuestionSaveRequest;
import com.tave.tavewebsite.domain.question.dto.request.QuestionUpdateRequest;
import com.tave.tavewebsite.domain.question.dto.response.QuestionDetailsResponse;
import com.tave.tavewebsite.domain.question.entity.Question;
import com.tave.tavewebsite.domain.question.exception.QuestionNotFoundException;
import com.tave.tavewebsite.domain.question.repository.QuestionRepository;
import com.tave.tavewebsite.global.common.FieldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public void saveQuestion(QuestionSaveRequest dto) {
        Question newQuestion = Question.from(dto);
        questionRepository.save(newQuestion);
    }

    @Transactional(readOnly = true)
    public List<QuestionDetailsResponse> findQuestionList() {
        return questionRepository.findAllByOrderByFieldTypeAscOrderedAscContentAsc()
                .stream()
                .map(QuestionDetailsResponse::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<QuestionDetailsResponse> findQuestionListByFieldType(FieldType fieldType) {
        return findQuestionsByFieldType(fieldType)
                .stream()
                .map(QuestionDetailsResponse::of)
                .toList();
    }

    @Transactional
    public void updateQuestion(QuestionUpdateRequest dto) {
        Question findQuestion = findQuestionById(dto.id());
        findQuestion.update(dto);
    }

    @Transactional
    public void deleteQuestionById(Long id) {
        Question question = findQuestionById(id);
        questionRepository.delete(question);
    }

    /*
    * Refactor
    * */

    public Question findQuestionById(Long id) {
        return questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
    }

    public List<Question> findQuestionsByFieldType(FieldType fieldType) {
        return questionRepository.findQuestionByFieldTypeOrderByOrderedAscContentAsc(fieldType);
    }
}
