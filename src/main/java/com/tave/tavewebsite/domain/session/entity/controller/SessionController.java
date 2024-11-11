package com.tave.tavewebsite.domain.session.entity.controller;

import com.tave.tavewebsite.domain.session.entity.dto.request.SessionRequestDto;
import com.tave.tavewebsite.domain.session.entity.dto.response.SessionResponseDto;
import com.tave.tavewebsite.domain.session.entity.service.SessionService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.tave.tavewebsite.domain.session.entity.controller.SuccessMessage.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    public SuccessResponse<SessionResponseDto> registerSession(@RequestBody SessionRequestDto sessionRequestDto, MultipartFile file){
        SessionResponseDto response = sessionService.saveSession(sessionRequestDto, file);

        return new SuccessResponse<>(response, SESSION_SUCCESS_REGISTER.getMessage());
    }


}
