package com.tave.tavewebsite.domain.interviewplace.dto.request;


public record InterviewPlaceSaveDto(

        String generalAddress,
        String detailAddress,
        String firstOpenChatLink,
        String secondOpenChatLink,
        String thirdOpenChatLink,
        String fourthOpenChatLink,
        String firstDocumentLink,
        String secondDocumentLink,
        String thirdDocumentLink,
        String fourthDocumentLink
) {
}
