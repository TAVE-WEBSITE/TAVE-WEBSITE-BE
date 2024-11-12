package com.tave.tavewebsite.domain.session.controller;

import com.tave.tavewebsite.domain.session.dto.request.SessionRequestDto;
import com.tave.tavewebsite.domain.session.dto.response.SessionResponseDto;
import com.tave.tavewebsite.domain.session.service.SessionService;
import com.tave.tavewebsite.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.tave.tavewebsite.domain.session.controller.SuccessMessage.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    public SuccessResponse<SessionResponseDto> registerSession(@RequestPart(name = "request") SessionRequestDto sessionRequestDto,
                                                               @RequestPart(name = "file", required = false) MultipartFile file){
        SessionResponseDto response = sessionService.saveSession(sessionRequestDto, file);

        return new SuccessResponse<>(response, SESSION_SUCCESS_REGISTER.getMessage());
    }

    @GetMapping("/{generation}/{page}/{size}")
    public SuccessResponse<List<SessionResponseDto>> getSession(@PathVariable String generation,
                                                                @PathVariable int page,
                                                                @PathVariable int size){
        List<SessionResponseDto> response = sessionService.findSessionByGeneration(generation, page, size);
        return new SuccessResponse<>(response, SESSION_SUCCESS_REGISTER.getMessage());
    }

    @DeleteMapping("/{sessionId}")
    public SuccessResponse<Void> deleteSession(@PathVariable Long sessionId){
        sessionService.deleteSession(sessionId);
        return new SuccessResponse<>(null, SESSION_DELETE.getMessage());
    }

}
