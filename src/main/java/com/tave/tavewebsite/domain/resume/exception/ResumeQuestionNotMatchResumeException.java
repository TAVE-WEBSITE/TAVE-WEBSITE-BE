package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.RESUME_QUESTION_NOT_MATCH_RESUME;

public class ResumeQuestionNotMatchResumeException extends BaseErrorException {
    public ResumeQuestionNotMatchResumeException() {
        super(RESUME_QUESTION_NOT_MATCH_RESUME.getCode(), RESUME_QUESTION_NOT_MATCH_RESUME.getMessage());
    }
}
