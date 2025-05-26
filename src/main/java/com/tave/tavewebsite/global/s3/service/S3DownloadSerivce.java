package com.tave.tavewebsite.global.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.tave.tavewebsite.domain.interviewfinal.dto.InterviewFormInputStreamDto;
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
    private static final String FINAL_INTERVIEW_FORM_NAME = "최종 면접 시간표 설정 양식.xlsx";
    private static final String HEADER_ATTACHMENT = "attachment";

    public S3DownloadSerivce(AmazonS3 s3Client,@Value("${bucket_name}") String bucketName, @Value("${final_interview_form}") String finalInterviewBasicFormUrl) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.finalInterviewBasicFormUrl = finalInterviewBasicFormUrl;
    }

    public InterviewFormInputStreamDto downloadInterviewFinalSetUpForm() throws IOException {

        S3Object s3Object = s3Client.getObject(bucketName, finalInterviewBasicFormUrl);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();


        HttpHeaders headers = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.builder(HEADER_ATTACHMENT)
                .filename(FINAL_INTERVIEW_FORM_NAME, StandardCharsets.UTF_8)
                .build();

        headers.setContentDisposition(contentDisposition);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return InterviewFormInputStreamDto.from(inputStream, headers, s3Object.getObjectMetadata().getContentLength());
    }
}
