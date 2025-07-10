package com.tave.tavewebsite.global.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import com.tave.tavewebsite.domain.interviewfinal.exception.EmptyFileException;
import com.tave.tavewebsite.domain.interviewfinal.exception.IsNotXlsxFileException;
import com.tave.tavewebsite.global.s3.exception.S3ErrorException.S3ConvertFailException;
import com.tave.tavewebsite.global.s3.exception.S3ErrorException.S3NotExistNameException;
import com.tave.tavewebsite.global.s3.exception.S3ErrorException.S3UploadFailException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.UUID;

@Service
@Slf4j
public class S3Service {

    private final AmazonS3 s3Client;
    private final String bucketName;
    private final String highQualityBucketName;
    private final String possibleTimeTableXLSX;
    private final String interviewTimeTableForManagerXLSX;


    private String key1;

    private static final String XLSX_APPLICATION_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public S3Service(AmazonS3 s3Client, @Value("${bucket_name}") String bucketName, @Value("${app.s3.high-quality-bucket}") String highQualityBucketName,
                     @Value("${interview_possible_time_table}") String interviewPossibleTimeTable,
                     @Value("${interview_time_table_for_manager}") String interviewTimeTableForManager
                     ) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.highQualityBucketName = highQualityBucketName;
        this.possibleTimeTableXLSX = interviewPossibleTimeTable;
        this.interviewTimeTableForManagerXLSX= interviewTimeTableForManager;
    }

    public URL uploadImages(MultipartFile file) {
        File convertFile = convertToWebp(file.getName(), file);
        String key = validateFileName(convertFile.getName(), bucketName);
        log.info("getName: {}", convertFile.getName());
        log.info("key: {}", key);
        // MultipartFile에서 InputStream을 얻어 S3에 업로드합니다.
        try (InputStream inputStream = new FileInputStream(convertFile)) {
            ObjectMetadata metadata = setMetaData(convertFile);
            // PutObjectRequest 생성 시 InputStream과 ContentType을 설정합니다.
            PutObjectRequest putRequest = new PutObjectRequest(bucketName, key, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(putRequest);
            convertFile.delete();
            return getImageUrl(key);
        } catch (IOException e) {
            throw new S3UploadFailException();
        }
    }

    public URL uploadHighQualityImages(MultipartFile file) {
        String key = validateFileName(file.getName(), highQualityBucketName);
        log.info("getName: {}", file.getName());
        log.info("key: {}", key);
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            log.info("ContentType: {}", file.getContentType());
            s3Client.putObject(highQualityBucketName, key, file.getInputStream(), metadata);

            return s3Client.getUrl(highQualityBucketName, key);

        } catch (IOException e) {
            throw new S3UploadFailException();
        }
    }

    public URL uploadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();

        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new RuntimeException("PDF 파일만 업로드할 수 있습니다.");
        }

        File uploadFile;
        try {
            uploadFile = convertMultipartFileToFile(file);

            String key = validateFileName(uploadFile.getName(), bucketName);

            try (InputStream inputStream = new FileInputStream(uploadFile)) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(uploadFile.length());
                metadata.setContentType(contentType); // application/pdf

                PutObjectRequest putRequest = new PutObjectRequest(bucketName, key, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);

                s3Client.putObject(putRequest);
                return getImageUrl(key);
            }
        } catch (IOException e) {
            throw new S3UploadFailException();
        } finally {
            new File(System.getProperty("java.io.tmpdir") + "/" + originalFilename).delete();
        }
    }

    public void uploadWorkbookToS3(Workbook workbook) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            byte[] bytes = out.toByteArray();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(XLSX_APPLICATION_TYPE);
            metadata.setContentLength(bytes.length);

            // S3 업로드
            PutObjectRequest request = new PutObjectRequest(bucketName, possibleTimeTableXLSX, inputStream, metadata);
            s3Client.putObject(request);

        } catch (IOException e) {
            throw new S3UploadFailException();
        }
    }

    public void uploadTimeTableForMangerXLSXToS3(MultipartFile file) {
        try {
            checkEmptyFile(file);
            checkIsXLSXFile(file);

            storeXLSXFile(file, interviewTimeTableForManagerXLSX);

        } catch (IOException e) {
            throw new S3UploadFailException();
        }
    }



    public URL getImageUrl(String key) {
        try {
            return s3Client.getUrl(bucketName, key);
        } catch (Exception e) {
            throw new S3NotExistNameException();
        }
    }

    public void deleteImage(String key) {
        try {
            s3Client.deleteObject(bucketName, key);
        } catch (Exception e) {
            throw new S3NotExistNameException();
        }
    }

    public File convertToWebp(String fileName, MultipartFile multipartFile) {
        File tempFile = null;
        try {
            // MultipartFile을 File로 변환
            tempFile = convertMultipartFileToFile(multipartFile);

            tempFile.setWritable(true);
            tempFile.setReadable(true);
            tempFile.setReadable(true, false);
            tempFile.setWritable(true, false);

            log.warn("체크 포인트 ============");

            WebpWriter webpWriter = new WebpWriter().withLossless();

            // WebP로 변환
            return ImmutableImage.loader() // 라이브러리 객체 생성
                    .fromFile(tempFile) // .jpg or .png File 가져옴
                    .output(webpWriter, new File(fileName + ".webp")); // 손실 압축 설정, fileName.webp로 파일 생성
        } catch (Exception e) {
            log.error("이미지 변환 에러 메세지: {}", e.getMessage(), e);
            throw new S3ConvertFailException();
        } finally {
            // 임시 파일 삭제
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }

    private ObjectMetadata setMetaData(File file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.length());
        metadata.setContentType("image/webp");

        return metadata;
    }

    private String validateFileName(String key, String bucketName) {
        if (s3Client.doesObjectExist(bucketName, key) || !StringUtils.hasText(key)) {
            log.info("getFileExtension 테스트 {}", getFileExtension(key));
            return UUID.randomUUID() + getFileExtension(key);
        }
        return key;
    }

    private String getFileExtension(String key) {
        int dotIndex = key.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < key.length() - 1) {
            return key.substring(dotIndex);
        }
        return ""; // 확장자가 없는 경우 빈 문자열 반환
    }

    private void storeXLSXFile(MultipartFile file, String key) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // S3에 업로드 (동일 키가 있으면 덮어쓰기)
        s3Client.putObject(bucketName, key, file.getInputStream(), metadata);
    }

    private void checkEmptyFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileException();
        }
    }

    private void checkIsXLSXFile(MultipartFile file) {
        if (!XLSX_APPLICATION_TYPE.equals(file.getContentType())) {
            throw new IsNotXlsxFileException();
        }
    }

}
