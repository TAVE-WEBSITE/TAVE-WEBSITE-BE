package com.tave.tavewebsite.domain.interviewfinal.usecase;

import com.tave.tavewebsite.domain.interviewfinal.dto.InterviewFinalConvertDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.InterviewFinalSaveDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.InterviewFormInputStreamDto;
import com.tave.tavewebsite.domain.interviewfinal.service.InterviewExcelService;
import com.tave.tavewebsite.domain.interviewfinal.service.InterviewSaveService;
import com.tave.tavewebsite.domain.member.dto.response.MemberResumeDto;
import com.tave.tavewebsite.domain.member.service.AdminService;
import com.tave.tavewebsite.domain.member.service.MemberService;
import com.tave.tavewebsite.global.s3.service.S3DownloadSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewFinalUseCase {

    private final InterviewExcelService interviewExcelService;
    private final InterviewSaveService interviewSaveService;
    private final S3DownloadSerivce s3DownloadSerivce;
    private final MemberService memberService;

    public InterviewFormInputStreamDto downloadInterviewFinal() throws IOException {
        return s3DownloadSerivce.downloadInterviewFinalSetUpForm();
    }


}
