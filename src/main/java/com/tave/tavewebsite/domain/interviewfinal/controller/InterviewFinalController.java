package com.tave.tavewebsite.domain.interviewfinal.controller;

import com.tave.tavewebsite.domain.interviewfinal.dto.InterviewFormInputStreamDto;
import com.tave.tavewebsite.domain.interviewfinal.usecase.InterviewFinalUseCase;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.tave.tavewebsite.domain.interviewfinal.controller.SuccessMessage.INTERVIEW_FINAL_CREATED;

@RestController
@RequiredArgsConstructor
public class InterviewFinalController {

    private final InterviewFinalUseCase interviewFinalUseCase;

    @GetMapping("/v1/manager/interview-final/form")
    public ResponseEntity<InputStreamResource> interviewFinalForm() throws IOException {

        InterviewFormInputStreamDto dto = interviewFinalUseCase.downloadInterviewFinal();

        return ResponseEntity.ok()
                .headers(dto.headers())
                .contentLength(dto.contentLength())
                .body(dto.inputStreamResource());
    }

    @PostMapping("/v1/manager/interview-final")
    public SuccessResponse interviewFinal(
            @RequestPart(name="file")MultipartFile file
    )
    {
        interviewFinalUseCase.insertInterviewEntityFromExcel(file);

        return SuccessResponse.ok(INTERVIEW_FINAL_CREATED.getMessage());
    }

}
