package com.tave.tavewebsite.global.s3.utils;

import com.tave.tavewebsite.global.s3.exception.S3ErrorException.S3NotExistNameException;

import java.net.URI;
import java.net.URISyntaxException;

public final class S3UrlUtils {

    private S3UrlUtils() {
    }

    // S3 객체 URL에서 디코딩된 object key(경로 전체) 추출
    public static String extractKey(String fileUrl) {
        try {
            String path = new URI(fileUrl).getPath();
            if (path == null || path.length() <= 1) {
                throw new S3NotExistNameException();
            }
            return path.substring(1);
        } catch (URISyntaxException e) {
            throw new S3NotExistNameException();
        }
    }
}
