package com.tave.tavewebsite.domain.study.controller;

import com.tave.tavewebsite.domain.study.dto.StudyReq;
import com.tave.tavewebsite.domain.study.dto.StudyResDto;
import com.tave.tavewebsite.domain.study.service.StudyService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @PostMapping("/manager/study")
    public SuccessResponse createStudy(@RequestPart @Valid StudyReq req, @RequestPart MultipartFile imageFile) {

        studyService.createStudy(req, imageFile);

        return SuccessResponse.ok("스터디가 생성되었습니다!");
    }

    @GetMapping("/study")
    public SuccessResponse<Page<StudyResDto>> getStudy(@PageableDefault(size = 8) Pageable pageable) {
        Page<StudyResDto> studies = studyService.getStudies(pageable);

        return new SuccessResponse<>(studies);
    }

    @PutMapping("/manager/{studyId}")
    public SuccessResponse updateStudy(@PathVariable Long studyId,
                                       @RequestBody StudyReq dto,
                                       @RequestPart MultipartFile imageFile) {
        studyService.modifyStudy(studyId, dto, imageFile);

        return SuccessResponse.ok("수정되었습니다.");
    }

    @DeleteMapping("/manager/{studyId}")
    public SuccessResponse deleteStudy(@PathVariable Long studyId) {
        studyService.deleteStudy(studyId);

        return SuccessResponse.ok("성공적으로 삭제되었습니다!");
    }
}
