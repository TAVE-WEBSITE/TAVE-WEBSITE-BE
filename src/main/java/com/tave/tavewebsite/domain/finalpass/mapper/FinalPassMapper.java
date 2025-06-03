package com.tave.tavewebsite.domain.finalpass.mapper;

import com.tave.tavewebsite.domain.finalpass.dto.request.FinalPassRequestDto;
import com.tave.tavewebsite.domain.finalpass.dto.response.FinalPassResponseDto;
import com.tave.tavewebsite.domain.finalpass.entity.FinalPass;

public class FinalPassMapper {

    public static FinalPass toEntity(FinalPassRequestDto dto) {
        return FinalPass.builder()
                .totalFee(dto.getTotalFee())
                .clubFee(dto.getClubFee())
                .mtFee(dto.getMtFee())
                .feeDeadline(dto.getFeeDeadline())
                .bankName(dto.getBankName())
                .accountNumber(dto.getAccountNumber())
                .accountHolder(dto.getAccountHolder())
                .surveyLink(dto.getSurveyLink())
                .surveyDeadline(dto.getSurveyDeadline())
                .otLink(dto.getOtLink())
                .otPassword(dto.getOtPassword())
                .otDeadline(dto.getOtDeadline())
                .build();
    }

    public static FinalPassResponseDto toResponseDto(FinalPass entity) {
        return FinalPassResponseDto.builder()
                .id(entity.getId())
                .totalFee(entity.getTotalFee())
                .clubFee(entity.getClubFee())
                .mtFee(entity.getMtFee())
                .feeDeadline(entity.getFeeDeadline())
                .bankName(entity.getBankName())
                .accountNumber(entity.getAccountNumber())
                .accountHolder(entity.getAccountHolder())
                .surveyLink(entity.getSurveyLink())
                .surveyDeadline(entity.getSurveyDeadline())
                .otLink(entity.getOtLink())
                .otPassword(entity.getOtPassword())
                .otDeadline(entity.getOtDeadline())
                .build();
    }
}