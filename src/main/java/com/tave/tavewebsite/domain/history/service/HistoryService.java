package com.tave.tavewebsite.domain.history.service;

import com.tave.tavewebsite.domain.history.dto.request.HistoryRequestDto;
import com.tave.tavewebsite.domain.history.dto.response.HistoryResponseDto;
import com.tave.tavewebsite.domain.history.dto.response.HistoryResponseDtoList;
import com.tave.tavewebsite.domain.history.entity.History;
import com.tave.tavewebsite.domain.history.exception.HistoryErrorException.HistoryNotFoundException;
import com.tave.tavewebsite.domain.history.repository.HistoryRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private static final int PRESENT_GENERATION =
            (LocalDate.now().getYear() - 2018) * 2;

    private final HistoryRepository historyRepository;

    @Transactional(readOnly = true)
    public List<HistoryResponseDtoList> findPublicOrderByGenerationDesc() {
        List<History> histories = historyRepository.findByIsPublic(true);
        Map<Integer, List<HistoryResponseDto>> historyMap = initializeMap(histories);

        List<HistoryResponseDtoList> result = new ArrayList<>();
        for (Entry<Integer, List<HistoryResponseDto>> integerListEntry : historyMap.entrySet()) {
            result.add(
                    HistoryResponseDtoList.of(String.valueOf(integerListEntry.getKey()), integerListEntry.getValue()));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<HistoryResponseDtoList> findAllOrderByGenerationDesc() {
        List<History> histories = historyRepository.findAll();
        Map<Integer, List<HistoryResponseDto>> historyMap = initializeMap(histories);

        List<HistoryResponseDtoList> result = new ArrayList<>();
        for (Entry<Integer, List<HistoryResponseDto>> integerListEntry : historyMap.entrySet()) {
            result.add(
                    HistoryResponseDtoList.of(String.valueOf(integerListEntry.getKey()), integerListEntry.getValue()));
        }
        return result;
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

    private Map<Integer, List<HistoryResponseDto>> initializeMap(List<History> histories) {
        Map<Integer, List<HistoryResponseDto>> historyMap = new HashMap<>();
        for (int i = 1; i <= PRESENT_GENERATION + 1; i++) {
            historyMap.put(i, new ArrayList<>());
        }
        for (History history : histories) {
            List<HistoryResponseDto> historyResponseDtos = historyMap.get(Integer.parseInt(history.getGeneration()));
            historyResponseDtos.add(HistoryResponseDto.of(history));
        }
        return historyMap;
    }
}