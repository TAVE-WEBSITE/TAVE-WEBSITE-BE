package com.tave.tavewebsite.domain.history.service;

import com.tave.tavewebsite.domain.apply.initial.setup.service.ApplyInitialSetupService;
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

    private static final int PRESENT_GENERATION = calculateGeneration();

    private final HistoryRepository historyRepository;
    private final ApplyInitialSetupService applyInitialSetupService;

    @Transactional(readOnly = true)
    public List<HistoryResponseDtoList> findPublic() {
        List<History> histories = historyRepository.findByIsPublic(true);
        Map<Integer, List<HistoryResponseDto>> historyMap = initializeMap(histories);

        return makeHistoryResponseDtoList(historyMap);
    }

    @Transactional(readOnly = true)
    public List<HistoryResponseDtoList> findAll() {
        List<History> histories = historyRepository.findAll();
        Map<Integer, List<HistoryResponseDto>> historyMap = initializeMap(histories);

        return makeHistoryResponseDtoList(historyMap);
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
        return historyRepository.findById(id)
                .orElseThrow(HistoryNotFoundException::new);
    }

    private Map<Integer, List<HistoryResponseDto>> initializeMap(List<History> histories) {
        Map<Integer, List<HistoryResponseDto>> historyMap = new HashMap<>();
        String generation = applyInitialSetupService.getCurrentGeneration();

        for (int i = 1; i <= Integer.parseInt(generation); i++) {
            historyMap.put(i, new ArrayList<>());
        }
        for (History history : histories) {
            List<HistoryResponseDto> historyResponseDtos = historyMap.get(Integer.parseInt(history.getGeneration()));
            historyResponseDtos.add(HistoryResponseDto.of(history));
        }
        return historyMap;
    }

    private List<HistoryResponseDtoList> makeHistoryResponseDtoList(Map<Integer, List<HistoryResponseDto>> historyMap) {
        List<HistoryResponseDtoList> result = new ArrayList<>();
        for (Entry<Integer, List<HistoryResponseDto>> integerListEntry : historyMap.entrySet()) {
            String suffix = getSuffix(integerListEntry.getKey());
            result.add(
                    HistoryResponseDtoList.of(integerListEntry.getKey() + suffix, integerListEntry.getValue()));
        }
        return result;
    }

    private String getSuffix(int number) {
        if (number % 10 == 1 && number % 100 != 11) {
            return "st";
        }
        if (number % 10 == 2 && number % 100 != 12) {
            return "nd";
        }
        if (number % 10 == 3 && number % 100 != 13) {
            return "rd";
        }
        return "th";
    }

    private static int calculateGeneration() {
        int year = (LocalDate.now().getYear() - 2018) * 2;
        if (LocalDate.now().getMonthValue() < 2) {
            year--;
        } else if (LocalDate.now().getMonthValue() > 7) {
            year++;
        }
        return year;
    }
}