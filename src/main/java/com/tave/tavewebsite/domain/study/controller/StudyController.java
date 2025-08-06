package com.tave.tavewebsite.domain.study.controller;


import com.tave.tavewebsite.domain.study.dto.StudyRequestDto;
import com.tave.tavewebsite.domain.study.dto.StudyResponseDto;
import com.tave.tavewebsite.domain.study.service.StudyService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static com.tave.tavewebsite.domain.study.controller.StudySuccessMessage.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @PostMapping("/manager/study")
    public SuccessResponse createStudy(@RequestBody @Valid StudyRequestDto req) {

        studyService.createStudy(req);

        return SuccessResponse.ok(STUDY_CREATE.getMessage());
    }

    @GetMapping("/normal/study")
    public SuccessResponse<Page<StudyResponseDto>> getStudy(@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable,
                                                            @RequestParam(defaultValue = "ALL", name = "generation") String generation,
                                                            @RequestParam(defaultValue = "ALL", name = "field") String field) {
        Page<StudyResponseDto> studies = studyService.getStudies(generation, field, pageable);

        return new SuccessResponse<>(
                studies,
                STUDY_GET.getMessage()
        );
    }

    @PutMapping("/manager/study/{studyId}")
    public SuccessResponse updateStudy(@PathVariable("studyId") Long studyId,
                                       @RequestBody @Valid StudyRequestDto dto) {
        studyService.modifyStudy(studyId, dto);

        return SuccessResponse.ok(STUDY_UPDATE.getMessage());
    }

    @DeleteMapping("/manager/study/{studyId}")
    public SuccessResponse deleteStudy(@PathVariable("studyId") Long studyId) {
        studyService.deleteStudy(studyId);

        return SuccessResponse.ok(STUDY_DELETE.getMessage());
    }
}
