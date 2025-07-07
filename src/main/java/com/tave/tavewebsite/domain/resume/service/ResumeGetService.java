package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeMemberInfoDto;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeTimeSlot;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeGetService {

    private final ResumeRepository resumeRepository;

    private static final String RESUME_CLASS_NAME = "resume";
    private static final String MEMBER_CLASS_NAME = "member";
    private static final String RESUME_TIME_SLOT_CLASS_NAME = "resumeTimeSlot";

    public Map<ResumeMemberInfoDto, List<LocalDateTime>> getResumeMemberAndInterviewTimeMap(String generation, EvaluationStatus status) {
        List<Tuple> tuples = getResumeListByGenerationAnd(generation, status);

        // List<Tuple> --> Map<ResumeMemberInfo, List<LocalDateTime>>
        return tuples.stream()
                .collect(Collectors.groupingBy(
                        tuple -> {
                            Resume r = tuple.get(RESUME_CLASS_NAME, Resume.class);
                            Member m = tuple.get(MEMBER_CLASS_NAME, Member.class);
                            return ResumeMemberInfoDto.from(r, m);
                        },
                        Collectors.mapping(
                                tuple -> {
                                    ResumeTimeSlot rs = tuple.get(RESUME_TIME_SLOT_CLASS_NAME, ResumeTimeSlot.class);
                                    return (rs != null ? rs.getInterviewDetailTime() : null);
                                },
                                Collectors.filtering(Objects::nonNull, Collectors.toList())
                        )
                ));
    }

    public List<Tuple> getResumeListByGenerationAnd(String generation, EvaluationStatus status) {
        return resumeRepository
                .findResumesWithInterviewTimesAndMemberByGenerationAndStatus(generation, status);
    }

}
