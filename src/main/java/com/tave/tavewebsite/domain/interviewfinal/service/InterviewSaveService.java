package com.tave.tavewebsite.domain.interviewfinal.service;

import com.tave.tavewebsite.domain.interviewfinal.dto.InterviewFinalConvertDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.InterviewFinalSaveDto;
import com.tave.tavewebsite.domain.interviewfinal.entity.InterviewFinal;
import com.tave.tavewebsite.domain.interviewfinal.repository.InterviewFinalJdbcRepository;
import com.tave.tavewebsite.domain.interviewfinal.repository.InterviewFinalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewSaveService {

    private final InterviewFinalJdbcRepository interviewFinalJdbcRepository;

    public void saveInterviewFinalList(List<InterviewFinalSaveDto> dto) {
        interviewFinalJdbcRepository.bulkInsert(dto);
    }

}
