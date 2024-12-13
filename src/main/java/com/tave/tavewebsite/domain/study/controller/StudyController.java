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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @PostMapping("/manager/study")
    public SuccessResponse createStudy(@RequestPart @Valid StudyRequestDto req, @RequestPart MultipartFile imageFile) {

        studyService.createStudy(req, imageFile);

        return SuccessResponse.ok("스터디가 생성되었습니다!");
    }

    @GetMapping("/normal/study")
    public SuccessResponse<Page<StudyResponseDto>> getStudy(@PageableDefault(size = 8) Pageable pageable,
                                                            @RequestParam(defaultValue = "ALL", name = "generation") String generation,
                                                            @RequestParam(defaultValue = "ALL", name = "field") String field) {
        Page<StudyResponseDto> studies = studyService.getStudies(generation, field, pageable);

        return new SuccessResponse<>(studies);
    }

    @PutMapping("/manager/study/{studyId}")
    public SuccessResponse updateStudy(@PathVariable("studyId") Long studyId,
                                       @RequestPart @Valid StudyRequestDto dto,
                                       @RequestPart MultipartFile imageFile) {
        studyService.modifyStudy(studyId, dto, imageFile);

        return SuccessResponse.ok("수정되었습니다.");
    }

    @DeleteMapping("/manager/study/{studyId}")
    public SuccessResponse deleteStudy(@PathVariable("studyId") Long studyId) {
        studyService.deleteStudy(studyId);

        return SuccessResponse.ok("성공적으로 삭제되었습니다!");
    }
}
