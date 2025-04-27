package com.tave.tavewebsite.domain.programinglaunguage.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

public abstract class LanguageErrorException {

    public static class NotFoundLanguageException extends BaseErrorException {
        public NotFoundLanguageException() {
            super(ProgrammingLanguageErrorMessage.NOT_FOUND_PROGRAMMING_LANGUAGE.getCode(),
                    ProgrammingLanguageErrorMessage.NOT_FOUND_PROGRAMMING_LANGUAGE.getMessage());
        }
    }

}