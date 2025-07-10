package com.tave.tavewebsite.global.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.tave.tavewebsite.domain.interviewfinal.dto.S3ExcelFileInputStreamDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class S3DownloadSerivce {

    private final AmazonS3 s3Client;
    private final String bucketName;
    private final String finalInterviewBasicFormUrl;
    private final String interviewPossibleTimeTable;
    private final String interviewTimeTableForManager;
    private final String interviewEvaluationInitialForm;
    private final String interviewEvaluationXLSX;
    private static final String FINAL_INTERVIEW_FORM_NAME = "최종 면접 시간표 설정 양식.xlsx";
    private static final String POSSIBLE_TIME_TABLE_FORM_NAME = "면접자 면접가능시간.xlsx";
    private static final String INTERVIEW_TIME_TABLE_FOR_MANAGER_FILE_NAME = "면접관 전용 면접시간표.xlsx";
    private static final String INTERVIEW_EVALUATION_INITIAL_FORM_NAME = "면접 평가 초기 양식.xlsx";
    private static final String INTERVIEW_EVALUATION_XLSX_NAME = "면접 평가 시트.xlsx";
    private static final String HEADER_ATTACHMENT = "attachment";

    public S3DownloadSerivce(AmazonS3 s3Client,@Value("${bucket_name}") String bucketName,
             @Value("${final_interview_form}") String finalInterviewBasicFormUrl,
             @Value("${interview_possible_time_table}") String interviewPossibleTimeTable,
             @Value("${interview_time_table_for_manager}") String interviewTimeTableForManager,
             @Value("${interview_evaluation_initial_form}") String interviewEvaluationInitialForm,
             @Value("${interview_evaluation_xlsx}") String interviewEvaluationXLSX

    ) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.finalInterviewBasicFormUrl = finalInterviewBasicFormUrl;
        this.interviewPossibleTimeTable = interviewPossibleTimeTable;
        this.interviewTimeTableForManager = interviewTimeTableForManager;
        this.interviewEvaluationInitialForm= interviewEvaluationInitialForm;
        this.interviewEvaluationXLSX = interviewEvaluationXLSX;
    }

    // 최종면접 설정 양식 다운로드
    public S3ExcelFileInputStreamDto downloadInterviewFinalSetUpForm() throws IOException {
            S3Object s3Object = s3Client.getObject(bucketName, finalInterviewBasicFormUrl);
            HttpHeaders headers = createHttpHeaders(FINAL_INTERVIEW_FORM_NAME);

            return S3ExcelFileInputStreamDto.from(
                    s3Object.getObjectContent(), headers, s3Object.getObjectMetadata().getContentLength()
            );
    }

    // 서류합격자들 면접 가능 시간 파악 엑셀
    public S3ExcelFileInputStreamDto downloadPossibleTimeTableXlsx() throws IOException {
        S3Object s3Object = s3Client.getObject(bucketName, interviewPossibleTimeTable);
        HttpHeaders headers = createHttpHeaders(POSSIBLE_TIME_TABLE_FORM_NAME);

        return S3ExcelFileInputStreamDto.from(
                s3Object.getObjectContent(), headers, s3Object.getObjectMetadata().getContentLength()
        );
    }

    // 면접 시간표 다운로드 (관리자용)
    public S3ExcelFileInputStreamDto downloadInterviewTimeTableForManagerXLSX() throws IOException {
        S3Object s3Object = s3Client.getObject(bucketName, interviewTimeTableForManager);
        HttpHeaders headers = createHttpHeaders(INTERVIEW_TIME_TABLE_FOR_MANAGER_FILE_NAME);

        return S3ExcelFileInputStreamDto.from(
                s3Object.getObjectContent(), headers, s3Object.getObjectMetadata().getContentLength()
        );
    }

    // 면접 평가 초기 양식 다운로드
    public S3ExcelFileInputStreamDto downloadInterviewEvaluationInitialFormXLSX() throws IOException {
        S3Object s3Object = s3Client.getObject(bucketName, interviewEvaluationInitialForm);
        HttpHeaders headers = createHttpHeaders(INTERVIEW_EVALUATION_INITIAL_FORM_NAME);

        return S3ExcelFileInputStreamDto.from(
                s3Object.getObjectContent(), headers, s3Object.getObjectMetadata().getContentLength()
        );
    }

    // 면접 평가 시트 다운로드 (질문 + 면접자 정보)
    public S3ExcelFileInputStreamDto downloadInterviewEvaluationXLSX() throws IOException {
        S3Object s3Object = s3Client.getObject(bucketName, interviewEvaluationXLSX);
        HttpHeaders headers = createHttpHeaders(INTERVIEW_EVALUATION_XLSX_NAME);

        return S3ExcelFileInputStreamDto.from(
                s3Object.getObjectContent(), headers, s3Object.getObjectMetadata().getContentLength()
        );
    }

    /*
    * refactor
    * */

    private HttpHeaders createHttpHeaders(String fileName) {
        HttpHeaders headers = new HttpHeaders();

        ContentDisposition contentDisposition = ContentDisposition.builder(HEADER_ATTACHMENT)
                .filename(fileName, StandardCharsets.UTF_8)
                .build();

        headers.setContentDisposition(contentDisposition);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        return headers;
    }
}
