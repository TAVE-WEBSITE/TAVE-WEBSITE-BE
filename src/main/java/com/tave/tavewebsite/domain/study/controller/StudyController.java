package com.tave.tavewebsite.domain.study.controller;

import com.tave.tavewebsite.domain.study.dto.StudyCreateReq;
import com.tave.tavewebsite.domain.study.dto.StudyModifyDto;
import com.tave.tavewebsite.domain.study.entity.Study;
import com.tave.tavewebsite.domain.study.service.StudyService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/study")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @PostMapping()
    public SuccessResponse createStudy(@RequestBody StudyCreateReq req) {

        studyService.createStudy(req);

        return SuccessResponse.ok("스터디가 생성되었습니다!");
    }

//    @GetMapping()
//    public SuccessResponse getStudy() {}

//    @PutMapping("/{studyId}")
//    public SuccessResponse updateStudy(@PathVariable Long studyId,
//                                       @RequestBody StudyModifyDto dto) {
//        studyService.modifyStudy(studyId, dto);
//    }
//
//    @DeleteMapping
//    public SuccessResponse deleteStudy(){
//
//    }
}
