package com.tave.tavewebsite.domain.resume.validator;

import com.tave.tavewebsite.domain.resume.exception.FileSizeExceededException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileValidator {
    private static final long MAX_FILE_SIZE = 100 * 1024 * 1024;

    public void validateSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileSizeExceededException();
        }
    }
}
