package com.tave.tavewebsite.global.success;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Getter
@AllArgsConstructor
@Builder
@ToString
@JsonPropertyOrder({"time", "status", "code", "message", "result"})
public class SuccessResponse<T> {

    @JsonProperty("status")
    private int status;

    private LocalDateTime time;
    private String code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public SuccessResponse(T result) {
        this.status = HttpStatus.OK.value();
        this.time = now();
        this.code = SuccessResponseStatus.SUCCESS.getCode();
        this.message = SuccessResponseStatus.SUCCESS.getMessage();
        this.result = result;
    }

    public SuccessResponse(T result, String msg){
        this.status = HttpStatus.OK.value();
        this.time = now();
        this.code = SuccessResponseStatus.SUCCESS.getCode();
        this.message = msg;
        this.result = result;
    }

    public static SuccessResponse ok(){
        return SuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .time(now())
                .code(SuccessResponseStatus.SUCCESS.getCode())
                .message("SUCCESS")
                .build();
    }

    public static SuccessResponse ok(String msg){
        return SuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .time(now())
                .code(SuccessResponseStatus.SUCCESS.getCode())
                .message(msg)
                .build();
    }
}
