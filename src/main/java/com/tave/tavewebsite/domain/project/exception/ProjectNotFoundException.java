package com.tave.tavewebsite.domain.project.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.project.exception.ErrorMessage.PROJECT_NOT_FOUND;

public class ProjectNotFoundException extends BaseErrorException {
    public ProjectNotFoundException() {
        super(PROJECT_NOT_FOUND.getCode(), PROJECT_NOT_FOUND.getMessage());
    }
}
