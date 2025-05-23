package com.tave.tavewebsite.domain.interviewplace.dto.request;


import jakarta.validation.constraints.NotBlank;

public record InterviewPlaceSaveDto(

        @NotBlank String generalAddress,
        @NotBlank String detailAddress,
        @NotBlank String firstOpenChatLink,
        @NotBlank String secondOpenChatLink,
        @NotBlank String thirdOpenChatLink,
        @NotBlank String fourthOpenChatLink,
        @NotBlank String firstDocumentLink,
        @NotBlank String secondDocumentLink,
        @NotBlank String thirdDocumentLink,
        @NotBlank String fourthDocumentLink
) {
}
