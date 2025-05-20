package com.tave.tavewebsite.domain.resume.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumeAnswerTempWrapper {
    private int page;  // 마지막에 저장한 페이지 번호
    private List<ResumeAnswerTempDto> answers;  // 해당 페이지의 답변 리스트
}
