package com.tave.tavewebsite.domain.resume.exception;

import com.tave.tavewebsite.global.exception.BaseErrorException;

import static com.tave.tavewebsite.domain.resume.exception.ErrorMessage.SOCIAL_LINK_NOT_FOUND;

public class SocialLinkNotFoundException extends BaseErrorException {
    public SocialLinkNotFoundException() {
        super(SOCIAL_LINK_NOT_FOUND.getCode(), SOCIAL_LINK_NOT_FOUND.getMessage());
    }
}
