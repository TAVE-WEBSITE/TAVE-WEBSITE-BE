package com.tave.tavewebsite.domain.session.service;

import com.tave.tavewebsite.domain.session.entity.Session;
import com.tave.tavewebsite.domain.session.dto.request.SessionRequestDto;
import com.tave.tavewebsite.domain.session.dto.response.SessionResponseDto;
import com.tave.tavewebsite.domain.session.repository.SessionRepository;
import com.tave.tavewebsite.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final S3Service s3Service;

    public SessionResponseDto saveSession(SessionRequestDto sessionRequestDto, MultipartFile file) {

        // 이미지 업로드 후 저장
        URL savedImageUrl = s3Service.uploadImages(file);
        log.info(savedImageUrl.toString());

        // RequestDto -> Entity -> save() -> ResponseDto
        Session savedSession = sessionRepository.save(Session.of(sessionRequestDto, savedImageUrl));
        return SessionResponseDto.from(savedSession);
    }

}
