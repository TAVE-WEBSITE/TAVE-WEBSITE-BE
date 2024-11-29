package com.tave.tavewebsite.domain.study.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.study.exception.ErrorMessage._NOT_FOUND_STUDY;

public class NotFoundStudy extends BaseErrorException {


    public NotFoundStudy() {
        super(_NOT_FOUND_STUDY.getCode(), _NOT_FOUND_STUDY.getMessage());
    }
}
