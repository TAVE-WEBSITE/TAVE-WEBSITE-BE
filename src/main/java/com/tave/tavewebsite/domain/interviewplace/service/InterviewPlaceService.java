package com.tave.tavewebsite.domain.interviewplace.service;

import com.tave.tavewebsite.domain.interviewplace.dto.request.InterviewPlaceSaveDto;
import com.tave.tavewebsite.domain.interviewplace.dto.response.InterviewPlaceResponse;
import com.tave.tavewebsite.domain.interviewplace.entity.InterviewPlace;
import com.tave.tavewebsite.domain.interviewplace.exception.NotFoundInterviewPlaceException;
import com.tave.tavewebsite.domain.interviewplace.repository.InterviewPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewPlaceService {

    private final InterviewPlaceRepository interviewPlaceRepository;

    public void saveInterviewPlace(List<InterviewPlaceSaveDto> dtoList) {

        List<InterviewPlace> saveList = dtoList.stream().map(InterviewPlace::of).toList();

        interviewPlaceRepository.saveAll(saveList);
    }

}
