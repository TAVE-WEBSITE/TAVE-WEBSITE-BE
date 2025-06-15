package com.tave.tavewebsite.domain.resume.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PersonalInfoCreateRequestDto {
    @NotNull
    @Size(min = 1, max = 20)
    private String school;

    @NotNull
    @Size(max = 30)
    private String major;

    @Size(max = 30)
    private String minor;

    @NotNull
    private String field;

    @NotNull(message = "generation 필수로 입력해주시기 바랍니다.")
    @Size(min = 1, max = 5, message = "1~5 글자 사이로 입력해주세요.")
    String generation;
}
