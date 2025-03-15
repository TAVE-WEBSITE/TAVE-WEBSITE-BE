package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotReqDto;
import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotResDto;
import com.tave.tavewebsite.domain.resume.entity.Resume;
import com.tave.tavewebsite.domain.resume.entity.TimeSlot;
import com.tave.tavewebsite.domain.resume.exception.ResumeNotFoundException;
import com.tave.tavewebsite.domain.resume.repository.ResumeRepository;
import com.tave.tavewebsite.domain.resume.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSlotServiceImpl implements TimeSlotService {

    private final ResumeRepository resumeRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Transactional
    @Override
    public void createTimeSlot(Long resumeId, List<TimeSlotReqDto> timeSlots) {
        Resume resume = findIfResumeExists(resumeId);

        timeSlots.forEach(timeSlotReqDto -> {
            TimeSlot timeSlot = TimeSlot.of(timeSlotReqDto.time(), resume);
            timeSlotRepository.save(timeSlot);
        });
    }

    @Override
    public void updateTimeSlot(Long resumeId, List<TimeSlotReqDto> timeSlots) {
        // 이력서가 사용자의 소유인지 확인
        Resume resume = findIfResumeExists(resumeId);
    }

    @Override
    public List<TimeSlotResDto> getTimeSlots(Long resumeId) {
        findIfResumeExists(resumeId);
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByResumeId(resumeId);


        return timeSlots.stream().map(TimeSlotResDto::from).toList();
    }


    private Resume findIfResumeExists(Long resumeId) {
        return resumeRepository.findById(resumeId).orElseThrow(ResumeNotFoundException::new);
    }
}
