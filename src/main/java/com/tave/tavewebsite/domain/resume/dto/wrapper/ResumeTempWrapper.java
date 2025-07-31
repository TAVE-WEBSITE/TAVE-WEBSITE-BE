package com.tave.tavewebsite.domain.resume.dto.wrapper;

import com.tave.tavewebsite.domain.resume.dto.request.ResumeReqDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeTempWrapper {
    private Long memberId;
    private ResumeReqDto page2;
    private ResumeReqDto page3;
    private int lastPage;
}