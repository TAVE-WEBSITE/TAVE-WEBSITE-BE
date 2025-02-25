package com.tave.tavewebsite.domain.emailnotification.service;

import com.tave.tavewebsite.domain.emailnotification.dto.request.EmailNotificationRequeestDto;
import com.tave.tavewebsite.domain.emailnotification.repository.EmailNotificationRepository;
import com.tave.tavewebsite.domain.emailnotification.util.EmailNotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final EmailNotificationRepository emailNotificationRepository;

    public void save(EmailNotificationRequeestDto dto) {
        emailNotificationRepository.save(EmailNotificationMapper.map(dto));
    }

}