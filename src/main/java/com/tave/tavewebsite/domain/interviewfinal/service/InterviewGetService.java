package com.tave.tavewebsite.domain.interviewfinal.service;

import com.tave.tavewebsite.domain.interviewfinal.entity.InterviewFinal;
import com.tave.tavewebsite.domain.interviewfinal.repository.InterviewFinalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewGetService {

    private final InterviewFinalRepository interviewFinalRepository;

    public Page<InterviewFinal> getInterviewFinalList(Pageable pageable) {
        return interviewFinalRepository.findAllByOrderByInterviewDayAscInterviewTimeAscUsernameAsc(pageable);
    }

}
