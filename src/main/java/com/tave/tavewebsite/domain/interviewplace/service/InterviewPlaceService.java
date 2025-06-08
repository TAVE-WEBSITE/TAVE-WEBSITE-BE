package com.tave.tavewebsite.domain.interviewplace.service;

import com.tave.tavewebsite.domain.interviewplace.dto.request.InterviewPlaceSaveDto;
import com.tave.tavewebsite.domain.interviewplace.dto.response.InterviewPlaceDetailDto;
import com.tave.tavewebsite.domain.interviewplace.entity.InterviewPlace;
import com.tave.tavewebsite.domain.interviewplace.exception.NotFoundInterviewPlaceException;
import com.tave.tavewebsite.domain.interviewplace.repository.InterviewPlaceRepository;
import com.tave.tavewebsite.global.common.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewPlaceService {

    private final InterviewPlaceRepository interviewPlaceRepository;

    public void saveInterviewPlace(List<InterviewPlaceSaveDto> dtoList) {
        // 기존에 있는 InterviewPlace는 비활성화로 변경 SOFT-DELETE
        getInterviewPlaceActiveList()
                .forEach(InterviewPlace::updateStatus);

        // 응답 요청에 대한 InterviewPlace 생성
        List<InterviewPlace> saveList = dtoList.stream().map(InterviewPlace::of).toList();

        interviewPlaceRepository.saveAll(saveList);
    }

    public List<InterviewPlace> getInterviewPlaceActiveList() {
        return interviewPlaceRepository.findByStatus(Status.ACTIVE);
    }

}
