package com.tave.tavewebsite.domain.interviewfinal.service;

import com.tave.tavewebsite.domain.interviewfinal.repository.InterviewFinalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewDeleteService {

    private final InterviewFinalRepository interviewFinalRepository;

    public void deleteAll() {
        interviewFinalRepository.deleteAll();
    }

}
