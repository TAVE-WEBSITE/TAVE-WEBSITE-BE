package com.tave.tavewebsite.domain.member.dto.request;

import jakarta.validation.constraints.NotNull;

public record ValidateEmailReq(
        @NotNull
        String email,
        String number
){
}
