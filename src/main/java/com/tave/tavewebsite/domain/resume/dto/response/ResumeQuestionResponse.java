package com.tave.tavewebsite.domain.resume.dto.response;

import java.util.List;


public record ResumeQuestionResponse(

        List<DetailResumeQuestionResponse> common,
        List<DetailResumeQuestionResponse> specific

) {
    public static ResumeQuestionResponse of(
            List<DetailResumeQuestionResponse> common,
            List<DetailResumeQuestionResponse> specific
    ) {
        return new ResumeQuestionResponse(common, specific);
    }
}
