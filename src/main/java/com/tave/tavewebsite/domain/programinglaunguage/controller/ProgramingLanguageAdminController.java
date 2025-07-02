package com.tave.tavewebsite.domain.programinglaunguage.controller;


import com.tave.tavewebsite.domain.programinglaunguage.dto.request.ProgramingLanguageRequestDto;
import com.tave.tavewebsite.domain.programinglaunguage.dto.response.ProgrammingLanguageResponseDto;
import com.tave.tavewebsite.domain.programinglaunguage.service.ProgramingLanguageAdminService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ProgramingLanguageAdminController {

    private final ProgramingLanguageAdminService programingLanguageAdminService;

    @GetMapping("/member/lan/field/{field}")
    public SuccessResponse<List<ProgrammingLanguageResponseDto>> getAllProgrammingLanguageByField(
            @PathVariable("field") String field) {
        return new SuccessResponse<>(programingLanguageAdminService.getAllProgramingLanguagesByField(field),
                ProgramingLanguageSuccessMessage.READ_LANGUAGE_SUCCESS.getMessage());
    }

    @PostMapping("/manager/lan")
    public SuccessResponse postProgrammingLanguage(@RequestBody ProgramingLanguageRequestDto requestDto) {
        programingLanguageAdminService.createProgramingLanguage(requestDto);
        return SuccessResponse.ok(ProgramingLanguageSuccessMessage.CREATE_LANGUAGE_SUCCESS.getMessage());
    }

    @DeleteMapping("/manager/lan/{id}")
    public SuccessResponse deleteProgrammingLanguageById(@PathVariable("id") Long id) {
        programingLanguageAdminService.deleteProgramingLanguage(id);
        return SuccessResponse.ok(ProgramingLanguageSuccessMessage.DELETE_LANGUAGE_SUCCESS.getMessage());
    }
}
