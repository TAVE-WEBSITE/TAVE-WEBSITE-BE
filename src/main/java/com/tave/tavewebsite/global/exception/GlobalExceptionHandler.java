package com.tave.tavewebsite.global.exception;


import com.tave.tavewebsite.global.exception.Response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 로그 형식
    private static final String LOG_FORMAT = "Class : {}, Code : {}, Message : {}";
    private static final int ERROR_CODE = 400;
    private static final int SERVER_ERROR_CODE = 500;

    // 사용자 정의 예외 처리
    @ExceptionHandler(BaseErrorException.class)
    public ResponseEntity<ExceptionResponse<Void>> handle(BaseErrorException e) {

        logWarning(e, e.getErrorCode());
        ExceptionResponse<Void> response = ExceptionResponse.fail(e.getErrorCode(), e.getMessage());

        return ResponseEntity
                .status(e.getErrorCode())
                .body(response);
    }

    // 서버 측 에러 (이외의 에러)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse<Void>> handle(Exception e) {

        logWarning(e, SERVER_ERROR_CODE);
        ExceptionResponse<Void> response = ExceptionResponse.fail(SERVER_ERROR_CODE, e.getMessage());

        return ResponseEntity
                .status(SERVER_ERROR_CODE)
                .body(response);        }

    // log.warn이 중복되어 리팩토링
    private void logWarning(Exception e, int errorCode) {
        log.warn(e.getMessage(), e);  // 전체 로그 출력, 운영 단계에서는 삭제
        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), errorCode, e.getMessage());
    }
}
