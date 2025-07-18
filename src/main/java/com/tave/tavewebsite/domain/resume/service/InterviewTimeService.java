package com.tave.tavewebsite.domain.resume.service;

import com.tave.tavewebsite.domain.apply.initial.setup.dto.response.ApplyInitialSetupReadResponseDto;
import com.tave.tavewebsite.domain.apply.initial.setup.service.ApplyInitialSetupService;
import com.tave.tavewebsite.domain.resume.dto.request.InterviewTimeReqDto;
import com.tave.tavewebsite.domain.resume.dto.response.InterviewTimeResponseDto;
import com.tave.tavewebsite.domain.resume.dto.timeslot.TimeSlotResDto;
import com.tave.tavewebsite.domain.resume.entity.InterviewTime;
import com.tave.tavewebsite.domain.resume.repository.InterviewTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewTimeService {

    private final InterviewTimeRepository interviewTimeRepository;
    private final ApplyInitialSetupService applyInitialSetupService;

    @Transactional
    public void createInterviewTime(InterviewTimeReqDto reqDto) {

        interviewTimeRepository.deleteAll();

        ApplyInitialSetupReadResponseDto initialSetup = applyInitialSetupService.getInitialSetup();

        LocalDate startDate = LocalDate.from(initialSetup.interviewStartDate());
        LocalDate endDate   = LocalDate.from(initialSetup.interviewEndDate());
        LocalTime startTime = LocalTime.parse(reqDto.startTime());
        LocalTime endTime   = LocalTime.parse(reqDto.endTime());
        int intervalMin = reqDto.progressTime();
        log.info("interval: {}", intervalMin);

        // 날짜 반복: startDate 부터 endDate 까지
        for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {

            // 시간 커서: startTime 에서 endTime 직전까지 intervalMin 만큼씩 증가
            LocalTime cursor = startTime;
            while (!cursor.isAfter(endTime)) {
                LocalDateTime slot = LocalDateTime.of(currentDate, cursor);
                log.info("slot: {}", slot);

                // 엔티티 생성
                InterviewTime it = InterviewTime.of(slot);
                interviewTimeRepository.save(it);

                cursor = cursor.plusMinutes(intervalMin);
            }
        }
    }

    public List<TimeSlotResDto> getAllInterviewTimes() {
        return interviewTimeRepository.findAllByOrderByInterviewDetailTimeAsc()
                .stream()
                .map(TimeSlotResDto::from)
                .toList();
    }

    /*
     * refactor
     * */

    public List<InterviewTimeResponseDto> distinctInterviewDay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d");

        return getDistinctInterviewDates().stream()
                .map(day -> InterviewTimeResponseDto.of(day.format(formatter), day))
                .toList();
    }

    public List<LocalDate> getDistinctInterviewDates() {
        return interviewTimeRepository.findDistinctInterviewDates();
    }

}
