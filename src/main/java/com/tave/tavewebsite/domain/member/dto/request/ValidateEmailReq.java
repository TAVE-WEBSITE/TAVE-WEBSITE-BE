package com.tave.tavewebsite.domain.member.dto.request;

public record ValidateEmailReq(
        String nickname,
        String email,
        String number
){
}
