package com.tave.tavewebsite.domain.history.service;

import com.tave.tavewebsite.domain.history.dto.request.HistoryRequestDto;
import com.tave.tavewebsite.domain.history.dto.response.HistoryResponseDto;
import com.tave.tavewebsite.domain.history.entity.History;
import com.tave.tavewebsite.domain.history.exception.HistoryErrorException.HistoryNotFoundException;
import com.tave.tavewebsite.domain.history.repository.HistoryRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    @Transactional(readOnly = true)
    public List<HistoryResponseDto> findPublicOrderByGenerationDesc() {
        List<History> isPublicOrderByGenerationDesc = historyRepository.findByIsPublicOrderByGenerationDesc(true);
        List<HistoryResponseDto> historyResponseDtos = new ArrayList<>();
        for (History history : isPublicOrderByGenerationDesc) {
            historyResponseDtos.add(HistoryResponseDto.of(history));
        }
        return historyResponseDtos;
    }

    @Transactional(readOnly = true)
    public List<HistoryResponseDto> findAllOrderByGenerationDesc() {
        List<History> orderByGenerationDesc = historyRepository.findAllByOrderByGenerationDesc();
        List<HistoryResponseDto> historyResponseDtos = new ArrayList<>();
        for (History history : orderByGenerationDesc) {
            historyResponseDtos.add(HistoryResponseDto.of(history));
        }
        return historyResponseDtos;
    }

    public void save(HistoryRequestDto dto) {
        historyRepository.save(dto.toHistory());
    }

    @Transactional
    public void patch(Long id, HistoryRequestDto dto) {
        History history = validate(id);
        history.patchHistory(dto);
    }

    public void delete(Long id) {
        validate(id);
        historyRepository.deleteById(id);
    }

    private History validate(Long id) {
        return historyRepository.findById(id).orElseThrow(HistoryNotFoundException::new);
    }
}