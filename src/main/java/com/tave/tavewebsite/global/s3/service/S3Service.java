package com.tave.tavewebsite.global.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class S3Service {

    private static final String LOG_FORMAT = "Class : {}, Code : {}, Message : {}";

    private final AmazonS3Client s3Client;
    private final String bucketName;

    public S3Service(AmazonS3Client s3Client, @Value("${BUCKET_NAME}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public void uploadImages(File file) {
        String key = file.getName();
        if (s3Client.doesObjectExist(bucketName, key)) {
            s3Client.deleteObject(bucketName, key);
        }
        s3Client.putObject(new PutObjectRequest(bucketName, key, file));
    }

    public URL getImageUrl(String key) {
        return s3Client.getUrl(bucketName, key);
    }

    public void deleteImage(String key) {
        s3Client.deleteObject(bucketName, key);
    }

}
