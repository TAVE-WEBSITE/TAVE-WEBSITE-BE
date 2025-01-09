package com.tave.tavewebsite.domain.history.controller;

import com.tave.tavewebsite.domain.history.dto.request.HistoryRequestDto;
import com.tave.tavewebsite.domain.history.dto.response.HistoryResponseDtoList;
import com.tave.tavewebsite.domain.history.service.HistoryService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/manager/history")
@RequiredArgsConstructor
public class ManagerHistoryController {

    private final HistoryService historyService;

    @GetMapping
    public SuccessResponse<List<HistoryResponseDtoList>> getAllHistory() {
        return new SuccessResponse<>(historyService.findAll());
    }

    @PostMapping
    public SuccessResponse postHistory(@RequestBody @Valid HistoryRequestDto historyRequestDto) {
        historyService.save(historyRequestDto);
        return SuccessResponse.ok(
                HistorySuccessMessage.POST_SUCCESS.getMessage());
    }

    @PatchMapping("/{historyId}")
    public SuccessResponse updateHistory(@PathVariable("historyId") Long id,
                                         @RequestBody @Valid HistoryRequestDto historyRequestDto) {
        historyService.patch(id, historyRequestDto);
        return SuccessResponse.ok(
                HistorySuccessMessage.UPDATE_SUCCESS.getMessage());
    }

    @DeleteMapping("/{historyId}")
    public SuccessResponse deleteHistory(@PathVariable("historyId") Long id) {
        historyService.delete(id);
        return SuccessResponse.ok(HistorySuccessMessage.DELETE_SUCCESS.getMessage());
    }
}
