package com.tave.tavewebsite.domain.history.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HistoryErrorMessage {

    NOT_FOUND_HISTORY(400, "History를 찾을 수 없습니다.");

    final int code;
    final String message;

}
