package com.tave.tavewebsite.domain.interviewplace.controller;

import com.tave.tavewebsite.domain.interviewplace.dto.request.InterviewPlaceSaveDto;
import com.tave.tavewebsite.domain.interviewplace.service.InterviewPlaceService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.tave.tavewebsite.domain.interviewplace.controller.SuccessMessage.*;

@RestController
@RequiredArgsConstructor
public class InterviewPlaceController {

    private final InterviewPlaceService interviewPlaceService;

    @PostMapping("/v1/manager/interview-place")
    public SuccessResponse saveInterviewPlaceList(@RequestBody @Valid List<InterviewPlaceSaveDto> dtoList) {

        interviewPlaceService.saveInterviewPlace(dtoList);

        return new SuccessResponse<>(INTERVIEW_PLACE_CREATED.getMessage());
    }

}
