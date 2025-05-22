package com.tave.tavewebsite.domain.interviewplace.dto.response;

import com.tave.tavewebsite.domain.interviewplace.entity.InterviewPlace;
import lombok.Builder;

@Builder
public record InterviewPlaceResponse(
        Long id,
        String generalAddress,
        String detailAddress,
        String firstOpenChatLink,
        String secondOpenChatLink,
        String thirdOpenChatLink,
        String fourthOpenChatLink,
        String firstDocumentLink,
        String secondDocumentLink,
        String thridDocumentLink,
        String fourthDocumentLink
) {
    public static InterviewPlaceResponse of(InterviewPlace interviewPlace) {
        return InterviewPlaceResponse.builder()
                .id(interviewPlace.getId())
                .generalAddress(interviewPlace.getGeneralAddress())
                .detailAddress(interviewPlace.getDetailAddress())
                .firstOpenChatLink(interviewPlace.getFirstOpenChatLink())
                .secondOpenChatLink(interviewPlace.getSecondOpenChatLink())
                .thirdOpenChatLink(interviewPlace.getThirdOpenChatLink())
                .fourthOpenChatLink(interviewPlace.getFourthOpenChatLink())
                .firstDocumentLink(interviewPlace.getFirstDocumentLink())
                .secondDocumentLink(interviewPlace.getSecondDocumentLink())
                .thridDocumentLink(interviewPlace.getThridDocumentLink())
                .fourthDocumentLink(interviewPlace.getFourthDocumentLink())
                .build();
    }
}
