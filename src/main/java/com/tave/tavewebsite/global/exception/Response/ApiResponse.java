package com.tave.tavewebsite.global.exception.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    private int code;
    private String message;
    private T data;

    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ApiResponse<T> fail(int code, String message) {

        return new ApiResponse<>(code, message);
    }

    /*
    * 응답 성공과 실패 형식을 통일 한다면 아래 메소드 사용
    * 1. code와 message는 응답 성공시 enum을 커스텀하여 넘겨준다.
    * 2. T data는 Dto를 넘겨주면 됩니다.
    * */
//    public static <T> ApiResponse<T> success(int code, String message, T data) {
//        return new ApiResponse<>(code, message, data);
//    }

}
