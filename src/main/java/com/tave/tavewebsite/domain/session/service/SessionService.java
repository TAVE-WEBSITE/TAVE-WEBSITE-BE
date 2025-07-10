package com.tave.tavewebsite.domain.session.service;

import com.tave.tavewebsite.domain.session.entity.Session;
import com.tave.tavewebsite.domain.session.dto.request.SessionRequestDto;
import com.tave.tavewebsite.domain.session.dto.response.SessionResponseDto;
import com.tave.tavewebsite.domain.session.exception.SessionNotFoundException;
import com.tave.tavewebsite.domain.session.repository.SessionRepository;
import com.tave.tavewebsite.domain.session.util.TimeUtil;
import com.tave.tavewebsite.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final TimeUtil timeUtil;
    private final S3Service s3Service;

    public SessionResponseDto saveSession(SessionRequestDto sessionRequestDto, MultipartFile file) {
        URL savedImageUrl = s3Service.uploadHighQualityImages(file);
        Session savedSession = sessionRepository.save(Session.of(sessionRequestDto, savedImageUrl, timeUtil));

        return SessionResponseDto.from(savedSession);
    }

    public List<SessionResponseDto> findSessionList() {

        return sessionRepository.findAllByOrderByEventDayAsc()
                .stream()
                .map(SessionResponseDto::from)
                .toList();
    }

    public void deleteSession(Long sessionId) {
        Session findSession = findBySessionId(sessionId);
        sessionRepository.delete(findSession);
    }

    @Transactional
    public void updateSession(Long sessionId, SessionRequestDto sessionRequestDto, MultipartFile file){
        Session findSession = findBySessionId(sessionId);
        updateSessionField(findSession, sessionRequestDto);
        updateSessionImgUrl(findSession, file);
    }

    /*
    * 리팩토링
    * */

    private Session findBySessionId(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(SessionNotFoundException::new);
    }

    private void updateSessionField(Session findSession, SessionRequestDto sessionRequestDto) {
        findSession.updateField(sessionRequestDto, timeUtil);
    }

    private void updateSessionImgUrl( Session findSession, MultipartFile file) {
        if(file != null){
            URL savedImageUrl = s3Service.uploadHighQualityImages(file);
            findSession.updateImgUrl(savedImageUrl);
        }
    }

}
