package com.tave.tavewebsite.domain.history.dto.request;

import com.tave.tavewebsite.domain.history.entity.History;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record HistoryRequestDto(
        @NotNull(message = "필수로 입력하셔야합니다.") @Size(min = 1, max = 5, message = "1~5 글자 사이로 입력해주세요.")
        String generation,
        @NotNull(message = "필수로 입력하셔야합니다.") @Size(min = 1, max = 500, message = "최대 500 글자까지 입력 가능합니다.")
        String description,
        String additionalDescription,
        @NotNull(message = "필수로 입력하셔야합니다.")
        Boolean isPublic
) {
    public History toHistory() {
        return History.builder()
                .generation(this.generation)
                .description(this.description)
                .additionalDescription(this.additionalDescription)
                .isPublic(this.isPublic)
                .build();
    }
}