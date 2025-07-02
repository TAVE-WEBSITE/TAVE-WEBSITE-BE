package com.tave.tavewebsite.domain.interviewfinal.controller;

import com.tave.tavewebsite.domain.interviewfinal.dto.S3ExcelFileInputStreamDto;
import com.tave.tavewebsite.domain.interviewfinal.usecase.InterviewExcelUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/v1/manager")
@RequiredArgsConstructor
public class InterviewExcelController {

    private final InterviewExcelUseCase useCase;

    @PostMapping("/excel/interviewer/time-table")
    public void createPossibleTimeTableExcel() {

        useCase.savePossibleInterviewTimeCSV();

    }

    @GetMapping("/excel/interviewer/time-table")
    public ResponseEntity<InputStreamResource> downloadPossibleTimeTableExcel() throws IOException {
        S3ExcelFileInputStreamDto dto = useCase.getPossibleInterviewTimeCSV();

        return ResponseEntity.ok()
                .headers(dto.headers())
                .contentLength(dto.contentLength())
                .body(dto.inputStreamResource());
    }

}
