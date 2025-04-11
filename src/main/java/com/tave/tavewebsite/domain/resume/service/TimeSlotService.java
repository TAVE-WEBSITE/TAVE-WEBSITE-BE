package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotResDto;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.TimeSlot;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
import com.tave.tavewebsite.domain.resume.exception.UnauthorizedResumeException;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.domain.resume.repository.TimeSlotRepository;
import com.tave.tavewebsite.global.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSlotService {

    private final ResumeRepository resumeRepository;
    private final TimeSlotRepository timeSlotRepository;

    private Member getCurrentMember() {
        return SecurityUtils.getCurrentMember();
    }

    @Transactional
    public void createTimeSlot(Long resumeId, List<LocalDateTime> timeSlots) {
        Resume resume = findIfResumeExists(resumeId);

        saveTimeSlot(timeSlots, resume);
    }

    @Transactional
    public void updateTimeSlot(Long resumeId, List<LocalDateTime> timeSlots) {
        Member currentMember = getCurrentMember();
        Resume resume = findIfResumeExists(resumeId);
        findIfResumeMine(currentMember, resume);

        timeSlotRepository.deleteAllByResumeId(resumeId);
        saveTimeSlot(timeSlots, resume);
    }

    public List<TimeSlotResDto> getTimeSlots(Long resumeId) {
        findIfResumeExists(resumeId);
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByResumeId(resumeId);


        return timeSlots.stream().map(TimeSlotResDto::from).toList();
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

    private void saveTimeSlot(List<LocalDateTime> timeSlots, Resume resume) {
        List<TimeSlot> result = timeSlots.stream().map(
                timeslot -> TimeSlot.of(timeslot, resume)
        ).toList();
        timeSlotRepository.saveAll(result);

//        timeSlots.forEach(timeSlotReqDto -> {
//            TimeSlot timeSlot = TimeSlot.of(timeSlotReqDto.time(), resume);
//            timeSlotRepository.save(timeSlot);
//        });
    }
}
