package com.tave.tavewebsite.domain.session.dto.request;

import com.tave.tavewebsite.domain.session.entity.Period;
import jakarta.validation.constraints.NotNull;

public record SessionRequestDto(

        @NotNull
        String title,
        @NotNull
        String description,

        String eventDay,

        @NotNull
        Period period

) {
}
