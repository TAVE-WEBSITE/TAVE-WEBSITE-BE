package com.tave.tavewebsite.domain.session.dto.request;

import jakarta.validation.constraints.NotNull;

public record SessionRequestDto(

        @NotNull
        String title,
        @NotNull
        String description,

        String eventDay

) {
}
