package com.tave.tavewebsite.domain.finalpass.service;

import com.tave.tavewebsite.domain.finalpass.dto.request.FinalPassRequestDto;
import com.tave.tavewebsite.domain.finalpass.dto.response.FinalPassResponseDto;
import com.tave.tavewebsite.domain.finalpass.entity.FinalPass;
import com.tave.tavewebsite.domain.finalpass.exception.FinalPassNotFoundException;
import com.tave.tavewebsite.domain.finalpass.mapper.FinalPassMapper;
import com.tave.tavewebsite.domain.finalpass.repository.FinalPassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FinalPassService {
    private final FinalPassRepository finalPassRepository;

    @Transactional
    public FinalPassResponseDto createFinalPass(FinalPassRequestDto dto) {
        FinalPass entity = FinalPassMapper.toEntity(dto);
        finalPassRepository.save(entity);
        return FinalPassMapper.toResponseDto(entity);
    }

    public FinalPassResponseDto getFinalPass() {
        FinalPass entity = finalPassRepository.findTopBy()
                .orElseThrow(FinalPassNotFoundException::new);
        return FinalPassMapper.toResponseDto(entity);
    }

}
