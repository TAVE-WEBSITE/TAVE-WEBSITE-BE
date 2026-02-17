package com.tave.tavewebsite.domain.apply.initial.setup.service;

import com.tave.tavewebsite.domain.apply.initial.setup.exception.ApplyInitialSetupException.ApplyInitialSetupNotFoundException;
import com.tave.tavewebsite.domain.apply.initial.setup.repository.ApplyInitialSetupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplyInitialSetUpGetService {

    private final ApplyInitialSetupRepository applyInitialSetupRepository;

    public String getCurrentGeneration() {
        return applyInitialSetupRepository.findById(1L)
                .orElseThrow(ApplyInitialSetupNotFoundException::new)
                .getGeneration();
    }

}
