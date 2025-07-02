package com.tave.tavewebsite.domain.interviewfinal.service;

import com.tave.tavewebsite.domain.interviewfinal.dto.InterviewFinalSaveDto;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeTimeSlot;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewFinalTestService {

    private final ResumeRepository resumeRepository;
    private final InterviewSaveService InterviewSaveService;

    public void saveResumePassListTestVersion() {
        List<Resume> resumeList = getResumePassList();

        List<InterviewFinalSaveDto> saveList = resumeList.stream()
                .filter(resume -> !resume.getResumeTimeSlots().isEmpty())
                .map(this::mapResumeToInterviewFinalSaveDto)
                .toList();


        InterviewSaveService.saveInterviewFinalList(saveList);
    }


    private List<Resume> getResumePassList() {
        return resumeRepository.findAllWithMemberAndTimeSlotsByStatus(EvaluationStatus.PASS);
    }

    public InterviewFinalSaveDto mapResumeToInterviewFinalSaveDto(Resume resume) {

        ResumeTimeSlot idxZeroTimeSlot = resume.getResumeTimeSlots().get(0);
        LocalDateTime interviewDateTime = idxZeroTimeSlot.getInterviewDetailTime();

        return InterviewFinalSaveDto.builder()
                .username(resume.getMember().getUsername())
                .email(resume.getMember().getEmail())
                .generation(resume.getResumeGeneration())
                .sex(resume.getMember().getSex())
                .fieldType(resume.getField())
                .university(resume.getSchool())
                .interviewDate(interviewDateTime.toLocalDate())
                .interviewTime(interviewDateTime.toLocalTime())
                .resumeId(resume.getId())
                .memberId(resume.getMember().getId())
                .build();
    }
}
