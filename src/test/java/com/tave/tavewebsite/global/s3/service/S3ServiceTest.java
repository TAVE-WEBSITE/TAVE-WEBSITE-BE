package com.tave.tavewebsite.global.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class S3ServiceTest {

    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        AmazonS3 s3Client = mock(AmazonS3.class);
        s3Service = new S3Service(s3Client, "hihi"); // 생성자 주입 방식
    }

    @DisplayName("MultipartFile을 .webp으로 업로드합니다.")
    @Test
    void convertToWebp() throws Exception {
     //given
        byte[] dummyImage = Files.readAllBytes(Paths.get("src/test/resources/sample.jpg"));
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "sample.jpg", "image/jpeg", dummyImage
        );

        String fileName = multipartFile.getOriginalFilename();

     //when
        File resultFile = s3Service.convertToWebp(fileName, multipartFile);
        System.out.println(resultFile.getName());

     //then
        assertThat(resultFile).exists();
        assertThat(resultFile.getName()).endsWith(".webp");

        resultFile.delete();
    }

}