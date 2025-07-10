package com.tave.tavewebsite.domain.interviewfinal.controller;

import com.tave.tavewebsite.domain.interviewfinal.dto.S3ExcelFileInputStreamDto;
import com.tave.tavewebsite.domain.interviewfinal.usecase.InterviewExcelUseCase;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.tave.tavewebsite.domain.interviewfinal.controller.SuccessMessage.INTERVIEW_TIME_TABLE_FOR_MANAGER_SAVED;

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

    // 면접관 전용 시간표 파일 업로드
    @PostMapping("/excel/interview/time-table")
    public SuccessResponse saveInterviewTimeTableForManager(
            @RequestPart(name="file") MultipartFile file
    ) {

        useCase.saveInterviewTimeTableForManagerXLSX(file);

        return SuccessResponse.ok(INTERVIEW_TIME_TABLE_FOR_MANAGER_SAVED.getMessage());
    }

    // 면접관 전용 시간표 파일 다운로드
    @GetMapping("/excel/interview/time-table")
    public ResponseEntity<InputStreamResource> downloadInterviewTimeTableForManager() throws IOException {
        S3ExcelFileInputStreamDto dto = useCase.getInterviewTimeTableForManagerXLSX();

        return ResponseEntity.ok()
                .headers(dto.headers())
                .contentLength(dto.contentLength())
                .body(dto.inputStreamResource());
    }

}
