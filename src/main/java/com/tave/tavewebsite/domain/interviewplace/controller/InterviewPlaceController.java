package com.tave.tavewebsite.domain.interviewplace.controller;

import com.tave.tavewebsite.domain.interviewplace.dto.request.InterviewPlaceSaveDto;
import com.tave.tavewebsite.domain.interviewplace.dto.response.InterviewPlaceResponse;
import com.tave.tavewebsite.domain.interviewplace.service.InterviewPlaceService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.tave.tavewebsite.domain.interviewplace.controller.SuccessMessage.*;

@RestController
@RequiredArgsConstructor
public class InterviewPlaceController {

    private final InterviewPlaceService interviewPlaceService;

    @PostMapping("/v1/manager/interview-place")
    public SuccessResponse<InterviewPlaceResponse> saveInterviewPlace(@RequestBody @Valid InterviewPlaceSaveDto dto) {

        InterviewPlaceResponse response = interviewPlaceService.saveInterviewPlace(dto);

        return new SuccessResponse<>(response, INTERVIEW_PLACE_CREATED.getMessage());
    }

    @GetMapping("/v1/manager/interview-place")
    public SuccessResponse<InterviewPlaceResponse> getInterviewPlace() {

        InterviewPlaceResponse response = interviewPlaceService.getInterviewPlace();

        return new SuccessResponse<>(response, INTERVIEW_PLACE_GET.getMessage());
    }

}
