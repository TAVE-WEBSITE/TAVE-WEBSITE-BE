package com.tave.tavewebsite.domain.programinglaunguage.controller;

import com.tave.tavewebsite.domain.programinglaunguage.dto.request.LanguageLevelRequestDto;
import com.tave.tavewebsite.domain.programinglaunguage.dto.response.LanguageLevelResponseDto;
import com.tave.tavewebsite.domain.programinglaunguage.service.ProgramingLanguageService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member")
@Validated
public class ProgramingLanguageController {

    private final ProgramingLanguageService programingLanguageService;

    @GetMapping("/lan/{id}")
    public SuccessResponse<List<LanguageLevelResponseDto>> getLanguageLevels(@PathVariable("id") Long id) {
        return new SuccessResponse<>(programingLanguageService.getLanguageLevel(id),
                ProgramingLanguageSuccessMessage.READ_LEVEL_SUCCESS.getMessage());
    }

    @PatchMapping("/lan/{id}")
    public SuccessResponse patchLanguageLevels(@PathVariable("id") Long id,
                                               @RequestBody @Valid List<LanguageLevelRequestDto> languageLevelRequestDtos) {
        programingLanguageService.patchLanguageLevel(id, languageLevelRequestDtos);
        return SuccessResponse.ok(ProgramingLanguageSuccessMessage.UPDATE_SUCCESS.getMessage());
    }
}
