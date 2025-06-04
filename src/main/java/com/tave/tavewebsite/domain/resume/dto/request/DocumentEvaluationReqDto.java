package com.tave.tavewebsite.domain.resume.dto.request;

import jakarta.validation.constraints.NotNull;

public record DocumentEvaluationReqDto(

        @NotNull(message = "double형으로 0~10점 이내로 점수를 부여해주세요.")
        double score,

        @NotNull(message = "개인 견해를 적어주세요")
        String opinion
) {
}
