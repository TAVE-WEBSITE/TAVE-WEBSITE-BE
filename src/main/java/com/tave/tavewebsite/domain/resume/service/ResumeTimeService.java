package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotReqDto;
import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotResDto;
import com.tave.tavewebsite.domain.resume.entity.InterviewTime;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.ResumeTimeSlot;
import com.tave.tavewebsite.domain.resume.exception.NotFoundInterviewTimeException;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.UnauthorizedResumeException;
import com.tave.tavewebsite.domain.resume.repository.InterviewTimeRepository;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.domain.resume.repository.ResumeTimeSlotRepository;
import com.tave.tavewebsite.global.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeTimeService {

    private final ResumeRepository resumeRepository;
    private final ResumeTimeSlotRepository resumeTimeSlotRepository;
    private final InterviewTimeRepository interviewTimeRepository;

    private Member getCurrentMember() {
        return SecurityUtils.getCurrentMember();
    }

    @Transactional
    public void createTimeSlot(Long resumeId, List<TimeSlotReqDto> timeSlots) {
        Member currentMember = getCurrentMember();
        Resume resume = findIfResumeExists(resumeId);
        findIfResumeMine(currentMember, resume);

        resumeTimeSlotRepository.deleteAllByResumeId(resumeId);
        saveTimeSlot(timeSlots, resume);
    }

    // 사용자가 체크한 면접 가능 시간대 조회
    public List<TimeSlotResDto> getTimeSlots(Long resumeId) {
        Member currentMember = getCurrentMember();
        Resume resume = findIfResumeExists(resumeId);
        findIfResumeMine(currentMember, resume);
        List<ResumeTimeSlot> resumeTimeSlots = resumeTimeSlotRepository.findAllByResumeId(resumeId);

        return resumeTimeSlots.stream().map(TimeSlotResDto::from).toList();
    }

    private void findIfResumeMine(Member currentMember, Resume resume) {
        Resume resumeByMember = resumeRepository.findByMember(currentMember).orElseThrow(ResumeNotFoundException::new);
        if(!resumeByMember.equals(resume)) {
            throw new UnauthorizedResumeException();
        }
    }

    private Resume findIfResumeExists(Long resumeId) {
        return resumeRepository.findById(resumeId).orElseThrow(ResumeNotFoundException::new);
    }

    // timeSlots 받은 각각의 객체에 대해 해당 시간이 InterviewTime 엔티티에 저장되어 있는지 확인
    private void saveTimeSlot(List<TimeSlotReqDto> timeSlots, Resume resume) {

        for(TimeSlotReqDto timeSlotReqDto : timeSlots) {

            InterviewTime interviewTime = interviewTimeRepository.findByInterviewDetailTime(timeSlotReqDto.time())
                    .orElseThrow(NotFoundInterviewTimeException::new);

            ResumeTimeSlot resumeTimeSlot = ResumeTimeSlot.of(resume, interviewTime);

            resume.addTimeSlot(resumeTimeSlot);
            resumeTimeSlotRepository.save(resumeTimeSlot);
        }

    }

    private String validateTimeSlot(LocalDateTime dateTime, int d) {
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        if(d == 0){
            return dateTime.format(yearFormatter);
        }
        else if(d == 1) {
            return dateTime.format(dateFormatter);
        }
        else return dateTime.format(timeFormatter);
    }
}
