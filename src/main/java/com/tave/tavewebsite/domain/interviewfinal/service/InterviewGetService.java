package com.tave.tavewebsite.domain.interviewfinal.service;

import com.tave.tavewebsite.domain.interviewfinal.entity.InterviewFinal;
import com.tave.tavewebsite.domain.interviewfinal.exception.NotFoundInterviewFinalByMemberIdException;
import com.tave.tavewebsite.domain.interviewfinal.repository.InterviewFinalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewGetService {

    private final InterviewFinalRepository interviewFinalRepository;

    public Page<InterviewFinal> getInterviewFinalPageableList(Pageable pageable) {
        return interviewFinalRepository.findAllByOrderByInterviewDateAscInterviewTimeAscUsernameAsc(pageable);
    }

    public List<InterviewFinal> getInterviewFinalList() {
        return interviewFinalRepository.findAllByOrderByInterviewDateAscInterviewTimeAscUsernameAsc();
    }

    public InterviewFinal getInterviewFinalByMemberId(Long memberId, String generation) {
        return interviewFinalRepository.findByMemberIdAndGeneration(memberId, generation)
                .orElseThrow(NotFoundInterviewFinalByMemberIdException::new);
    }

    public List<InterviewFinal> getInterviewFinalListByGeneration(String generation) {
        return interviewFinalRepository
                .findAllByGenerationOrderByInterviewDateTimeUsername(generation);
    }

    public List<InterviewFinal> getInterviewFinalByDateAndTime(LocalDate interviewDate, LocalTime interviewTime) {
        return interviewFinalRepository.findByInterviewDateAndInterviewTime(interviewDate, interviewTime);
    }

}
