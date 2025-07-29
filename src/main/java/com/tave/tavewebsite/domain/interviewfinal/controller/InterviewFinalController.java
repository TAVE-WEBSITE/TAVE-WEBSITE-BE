package com.tave.tavewebsite.domain.interviewfinal.controller;

import com.tave.tavewebsite.domain.interviewfinal.dto.S3ExcelFileInputStreamDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.response.InterviewFinalDetailDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.response.InterviewFinalEvaluateResDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.response.InterviewFinalForMemberDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.response.InterviewFinalPageDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.response.timetable.InterviewTimeTableDto;
import com.tave.tavewebsite.domain.interviewfinal.usecase.InterviewFinalUseCase;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import com.tave.tavewebsite.global.common.FieldType;
import com.tave.tavewebsite.global.security.CurrentMember;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.tave.tavewebsite.domain.interviewfinal.controller.SuccessMessage.*;

@RestController
@RequiredArgsConstructor
public class InterviewFinalController {

    private final InterviewFinalUseCase interviewFinalUseCase;

    @GetMapping("/v1/manager/interview-final/form")
    public ResponseEntity<InputStreamResource> interviewFinalForm() throws IOException {

        S3ExcelFileInputStreamDto dto = interviewFinalUseCase.downloadInterviewFinal();

        return ResponseEntity.ok()
                .headers(dto.headers())
                .contentLength(dto.contentLength())
                .body(dto.inputStreamResource());
    }

    @GetMapping("/v1/member/interview-final/{generation}")
    public SuccessResponse<InterviewFinalForMemberDto> getMemberInterviewInfo(
            @CurrentMember Member currentMember,
            @PathVariable String generation
    ){

        InterviewFinalForMemberDto response = interviewFinalUseCase.getMemberInterviewFinalDetail(currentMember, generation);

        return new SuccessResponse<>(response,INTERVIEW_FINAL_MEMBER_INFO.getMessage());
    }

    @PostMapping("/v1/manager/interview-final")
    public SuccessResponse interviewFinal(
            @RequestPart(name="file")MultipartFile file
    )
    {
        interviewFinalUseCase.insertInterviewEntityFromExcel(file);

        return SuccessResponse.ok(INTERVIEW_FINAL_CREATED.getMessage());
    }

    @DeleteMapping("/v1/manager/interview-final")
    public SuccessResponse interviewFinalDelete() {

        interviewFinalUseCase.deleteAll();

        return SuccessResponse.ok(INTERVIEW_FINAL_DELETE_ALL.getMessage());
    }

    @GetMapping("/v1/manager/interview-final")
    public SuccessResponse<InterviewFinalPageDto> interviewFinalPageNation(
        @RequestParam int pageNum,
        @RequestParam int pageSize
    ) {
        InterviewFinalPageDto response = interviewFinalUseCase.getInterviewFinalList(pageNum, pageSize);

        return new SuccessResponse<>(response, INTERVIEW_FINAL_LIST_GET.getMessage());
    }

    @GetMapping("/v1/manager/interview-final/time-table/{generation}")
    public SuccessResponse<InterviewTimeTableDto> getinterviewFinalTimeTableList(
            @PathVariable String generation
    ){

        InterviewTimeTableDto response = interviewFinalUseCase.getTimeTableList(generation);

        return new SuccessResponse<>(response,INTERVIEW_FINAL_TIME_TABLE_LIST.getMessage());
    }

    @PostMapping("/v1/manager/test/interview-final")
    public SuccessResponse saveInterviewFinalTest() {

        interviewFinalUseCase.getInterviewFinalSaveListTestVersion();

        return new SuccessResponse<>(BETA_INTERVIEW_FINAL_LIST_CREATED.getMessage());
    }

    @GetMapping("/v1/admin/interview-final")
    public SuccessResponse<InterviewFinalEvaluateResDto> getAdminInterviewFinalDetail(
            @RequestParam(required = false) EvaluationStatus status,
            @RequestParam(required = false) FieldType type,
            @PageableDefault(size = 7) Pageable pageable
    ) {

        return new SuccessResponse<>(interviewFinalUseCase.getInterviewFinalEvaluateList(pageable, status, type),
                INTERVIEW_FINAL_LIST_GET.getMessage());
    }

    @PostMapping("/v1/admin/interview-final/{interviewFinalId}")
    public SuccessResponse updateInterviewFinalStatus(
            @PathVariable(name = "interviewFinalId") Long id,
            @RequestParam(name = "status") EvaluationStatus status
    ) {
        interviewFinalUseCase.updateFinalInterviewStatus(id, status);

        return new SuccessResponse<>(INTERVIEW_FINAL_UPDATE_SUCCESS.getMessage());
    }

}
