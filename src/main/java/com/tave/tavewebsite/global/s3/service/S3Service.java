package com.tave.tavewebsite.global.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tave.tavewebsite.global.s3.exception.S3ErrorException.S3NotExistNameException;
import com.tave.tavewebsite.global.s3.exception.S3ErrorException.S3UploadFailException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    private final AmazonS3 s3Client;
    private final String bucketName;

    public S3Service(AmazonS3 s3Client, @Value("${bucket_name}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public URL uploadImages(MultipartFile file) {
        String key = file.getOriginalFilename();
        checkExistFile(key);
        // MultipartFile에서 InputStream을 얻어 S3에 업로드합니다.
        try (InputStream inputStream = file.getInputStream()) {
            ObjectMetadata metadata = setMetaData(file);

            // PutObjectRequest 생성 시 InputStream과 ContentType을 설정합니다.
            PutObjectRequest putRequest = new PutObjectRequest(bucketName, key, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(putRequest);
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

    private ObjectMetadata setMetaData(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        return metadata;
    }

    private void checkExistFile(String key) {
        if (s3Client.doesObjectExist(bucketName, key)) {
            s3Client.deleteObject(bucketName, key);
        }
    }

}
