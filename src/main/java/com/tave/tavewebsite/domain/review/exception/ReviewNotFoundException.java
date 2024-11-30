package com.tave.tavewebsite.domain.review.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.review.exception.ErrorMessgae.REVIEW_NOT_FOUND;

public class ReviewNotFoundException extends BaseErrorException {

    public ReviewNotFoundException(){
        super(REVIEW_NOT_FOUND.getCode(), REVIEW_NOT_FOUND.getMessage());
    }
}
