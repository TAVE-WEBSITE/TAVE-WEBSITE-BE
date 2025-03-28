package com.tave.tavewebsite.global.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import com.tave.tavewebsite.global.s3.exception.S3ErrorException.S3ConvertFailException;
import com.tave.tavewebsite.global.s3.exception.S3ErrorException.S3NotExistNameException;
import com.tave.tavewebsite.global.s3.exception.S3ErrorException.S3UploadFailException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class S3Service {

    private final AmazonS3 s3Client;
    private final String bucketName;

    public S3Service(AmazonS3 s3Client, @Value("${bucket_name}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public URL uploadImages(MultipartFile file) {
        File convertFile = convertToWebp(file.getName(), file);
        String key = validateFileName(convertFile.getName());
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

            // WebP로 변환
            return ImmutableImage.loader() // 라이브러리 객체 생성
                    .fromFile(tempFile) // .jpg or .png File 가져옴
                    .output(WebpWriter.DEFAULT, new File(fileName + ".webp")); // 손실 압축 설정, fileName.webp로 파일 생성
        } catch (Exception e) {
            log.error("이미지 변환 에러 메세지", e.getMessage());
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

    private String validateFileName(String key) {
        if (!s3Client.doesObjectExist(bucketName, key) && !StringUtils.hasText(key)) {
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

}
