package com.tave.tavewebsite.domain.interviewfinal.controller;

import com.tave.tavewebsite.domain.interviewfinal.dto.S3ExcelFileInputStreamDto;
import com.tave.tavewebsite.domain.interviewfinal.usecase.InterviewExcelUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InterviewExcelController {

    private final InterviewExcelUseCase useCase;

    @PostMapping("/v1/normal/excel/interviewer/time-table")
    public void createPossibleTimeTableExcel() {

        useCase.savePossibleInterviewTimeCSV();

    }

}
