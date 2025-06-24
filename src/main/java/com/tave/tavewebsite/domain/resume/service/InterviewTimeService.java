package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.resume.dto.request.InterviewTimeReqDto;
import com.tave.tavewebsite.domain.resume.dto.response.InterviewTimeResponseDto;
import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotReqDto;
import com.tave.tavewebsite.domain.resume.entity.InterviewTime;
import com.tave.tavewebsite.domain.resume.entity.ResumeTimeSlot;
import com.tave.tavewebsite.domain.resume.repository.InterviewTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewTimeService {

    private final InterviewTimeRepository interviewTimeRepository;

    public void createInterviewTime(InterviewTimeReqDto reqDto) {

        interviewTimeRepository.deleteAll();

        Long resumeId = reqDto.resumeId();

        LocalDate startDate = reqDto.startDate();
        LocalDate endDate   = reqDto.endDate();
        LocalTime startTime = LocalTime.parse(reqDto.startTime());
        LocalTime endTime   = LocalTime.parse(reqDto.endTime());
        int intervalMin = reqDto.progressTime();

        // 날짜 반복: startDate 부터 endDate 까지
        for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {

            // 시간 커서: startTime 에서 endTime 직전까지 intervalMin 만큼씩 증가
            LocalTime cursor = startTime;
            while (!cursor.isAfter(endTime)) {
                LocalDateTime slot = LocalDateTime.of(currentDate, cursor);

                // 엔티티 생성
                InterviewTime it = InterviewTime.of(slot, resumeId);
                interviewTimeRepository.save(it);

                cursor = cursor.plusMinutes(intervalMin);
            }
        }
    }

    public List<InterviewTimeResponseDto> distinctInterviewDay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d");

        return getDistinctInterviewDates().stream()
                .map(day -> {
                    return InterviewTimeResponseDto.of(day.format(formatter), day);
                })
                .toList();
    }

    public List<LocalDate> getDistinctInterviewDates() {
        return interviewTimeRepository.findDistinctInterviewDates();
    }

    public List<TimeSlotReqDto> getTimeSlotsByResumeId(Long resumeId) {
        List<InterviewTime> interviewTimes = interviewTimeRepository.findByResumeId(resumeId);
        return interviewTimes.stream()
                .map(interviewTime -> new TimeSlotReqDto(interviewTime.getInterviewDetailTime()))
                .toList();
    }

    public List<TimeSlotReqDto> convertToDtoListFromTimeSlots(List<ResumeTimeSlot> timeSlots) {
        if (timeSlots == null) return null;
        return timeSlots.stream()
                .map(ts -> new TimeSlotReqDto(ts.getInterviewTime().getInterviewDetailTime()))
                .toList();
    }

}
