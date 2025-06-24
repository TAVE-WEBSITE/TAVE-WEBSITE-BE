package com.tave.tavewebsite.domain.session.controller;

import static com.tave.tavewebsite.domain.session.controller.SuccessMessage.SESSION_DELETE;
import static com.tave.tavewebsite.domain.session.controller.SuccessMessage.SESSION_SUCCESS_GET;
import static com.tave.tavewebsite.domain.session.controller.SuccessMessage.SESSION_SUCCESS_REGISTER;
import static com.tave.tavewebsite.domain.session.controller.SuccessMessage.SESSION_UPDATE;

import com.tave.tavewebsite.domain.session.dto.request.SessionRequestDto;
import com.tave.tavewebsite.domain.session.dto.response.SessionResponseDto;
import com.tave.tavewebsite.domain.session.service.SessionService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/manager/session")
    public SuccessResponse<SessionResponseDto> registerSession(
            @Valid @RequestPart(name = "request") SessionRequestDto sessionRequestDto,
            @RequestPart(name = "file", required = false) MultipartFile file) {

        SessionResponseDto response = sessionService.saveSession(sessionRequestDto, file);

        return new SuccessResponse<>(response, SESSION_SUCCESS_REGISTER.getMessage());
    }

    @GetMapping("/normal/session")
    public SuccessResponse<List<SessionResponseDto>> getSessionList() {

        List<SessionResponseDto> response = sessionService.findSessionList();

        return new SuccessResponse<>(response, SESSION_SUCCESS_GET.getMessage());
    }

    @DeleteMapping("/manager/session/{sessionId}")
    public SuccessResponse<Void> deleteSession(@PathVariable Long sessionId) {

        sessionService.deleteSession(sessionId);

        return new SuccessResponse<>(null, SESSION_DELETE.getMessage());
    }

    @PatchMapping("/manager/session/{sessionId}")
    public SuccessResponse<Void> updateSession(@PathVariable Long sessionId,
                                               @Valid @RequestPart(name = "request") SessionRequestDto sessionRequestDto,
                                               @RequestPart(name = "file", required = false) MultipartFile file) {

        sessionService.updateSession(sessionId, sessionRequestDto, file);

        return new SuccessResponse<>(null, SESSION_UPDATE.getMessage());
    }


}
