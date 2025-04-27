package com.tave.tavewebsite.domain.resume.dto.request;

import com.tave.tavewebsite.global.common.FieldType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;


@Getter
public class PersonalInfoRequestDto {
    @NotNull
    @Size(min = 1, max = 20)
    private String school;

    @NotNull
    @Size(max = 30)
    private String major;

    @Size(max = 30)
    private String minor;

    @NotNull
    private FieldType field;
}
