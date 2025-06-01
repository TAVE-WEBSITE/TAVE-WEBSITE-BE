package com.tave.tavewebsite.domain.interviewplace.service;

import com.tave.tavewebsite.domain.interviewplace.dto.request.InterviewPlaceSaveDto;
import com.tave.tavewebsite.domain.interviewplace.dto.response.InterviewPlaceResponse;
import com.tave.tavewebsite.domain.interviewplace.entity.InterviewPlace;
import com.tave.tavewebsite.domain.interviewplace.exception.NotFoundInterviewPlaceException;
import com.tave.tavewebsite.domain.interviewplace.repository.InterviewPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewPlaceService {

    private final InterviewPlaceRepository interviewPlaceRepository;

    public InterviewPlaceResponse saveInterviewPlace(InterviewPlaceSaveDto dto) {

        InterviewPlace interviewPlace = InterviewPlace.of(dto);

        return InterviewPlaceResponse.of(interviewPlaceRepository.save(interviewPlace));
    }

    public InterviewPlaceResponse getInterviewPlace() {
        InterviewPlace findEntity = interviewPlaceRepository.findFirstByOrderByCreatedAtDesc()
                .orElseThrow(NotFoundInterviewPlaceException::new);

        return InterviewPlaceResponse.of(findEntity);
    }

}
