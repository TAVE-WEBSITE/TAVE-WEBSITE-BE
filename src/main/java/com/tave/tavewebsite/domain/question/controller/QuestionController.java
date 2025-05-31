package com.tave.tavewebsite.domain.question.controller;

import com.tave.tavewebsite.domain.question.dto.request.QuestionSaveRequest;
import com.tave.tavewebsite.domain.question.dto.request.QuestionSwapRequest;
import com.tave.tavewebsite.domain.question.dto.request.QuestionUpdateRequest;
import com.tave.tavewebsite.domain.question.dto.response.QuestionDetailsResponse;
import com.tave.tavewebsite.domain.question.service.QuestionService;
import com.tave.tavewebsite.global.common.FieldType;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tave.tavewebsite.domain.question.controller.SuccessMessage.*;

@Slf4j
@RequestMapping("/v1/manager")
@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/question")
    public SuccessResponse questionSave(@RequestBody @Valid QuestionSaveRequest dto) {

        questionService.saveQuestion(dto);

        return SuccessResponse.ok(QUESTION_CREATED.getMessage());
    }

    @GetMapping("/question")
    public SuccessResponse<List<QuestionDetailsResponse>> questionListAll() {

        List<QuestionDetailsResponse> response =
                questionService.findQuestionList();

        return new SuccessResponse<>(response, QUESTION_ALL_LIST.getMessage());
    }

    @GetMapping("/question/{fieldType}")
    public SuccessResponse<List<QuestionDetailsResponse>> questionFieldTypeList(
        @PathVariable FieldType fieldType
    ) {

        List<QuestionDetailsResponse> response = questionService.findQuestionListByFieldType(fieldType);

        return new SuccessResponse<>(response, QUESTION_FIELD_LIST.getMessage());
    }

    @PatchMapping("/question")
    public SuccessResponse questionUpdate(@RequestBody @Valid QuestionUpdateRequest dto) {

        questionService.updateQuestion(dto);

        return SuccessResponse.ok(QUESTION_UPDATED.getMessage());
    }

    @PatchMapping("/question/swap")
    public SuccessResponse swapQuestionOrdered(@RequestBody QuestionSwapRequest dto) {

        questionService.swapQuestionsOrdered(dto);

        return SuccessResponse.ok(QUESTION_ORDERED_SWAP.getMessage());
    }

    @DeleteMapping("/question/{questionId}")
    public SuccessResponse questionDelete(@PathVariable Long questionId) {

        questionService.deleteQuestionById(questionId);

        return SuccessResponse.ok(QUESTION_DELETED.getMessage());
    }

    // todo 질문 별로 순서 바꾸는 로직 필요함.
}
