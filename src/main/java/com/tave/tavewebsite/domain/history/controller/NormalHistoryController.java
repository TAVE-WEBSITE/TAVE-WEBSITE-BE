package com.tave.tavewebsite.domain.history.controller;

import com.tave.tavewebsite.domain.history.dto.response.HistoryResponseDtoList;
import com.tave.tavewebsite.domain.history.service.HistoryService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/normal/history")
public class NormalHistoryController {

    private final HistoryService historyService;

    @GetMapping
    public SuccessResponse<List<HistoryResponseDtoList>> getPublicHistory() {
        return new SuccessResponse<>(historyService.findPublic());
    }
}
