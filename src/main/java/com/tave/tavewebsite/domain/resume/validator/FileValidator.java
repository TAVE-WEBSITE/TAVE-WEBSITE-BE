package com.tave.tavewebsite.domain.resume.validator;

import com.tave.tavewebsite.domain.resume.exception.FileSizeExceededException;
import com.tave.tavewebsite.domain.resume.exception.PdfFileTypeException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileValidator {
    private static final long MAX_FILE_SIZE = 200L * 1024 * 1024;
    public static final String PDF_CONTENT_TYPE = "application/pdf";

    public void validateSize(MultipartFile file) {
        validateSize(file.getSize());
    }

    public void validateSize(Long fileSize) {
        if (fileSize == null || fileSize <= 0 || fileSize > MAX_FILE_SIZE) {
            throw new FileSizeExceededException();
        }
    }

    public void validatePdfContentType(String contentType) {
        if (!PDF_CONTENT_TYPE.equals(contentType)) {
            throw new PdfFileTypeException();
        }
    }
}
