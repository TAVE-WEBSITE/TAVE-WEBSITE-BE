package com.tave.tavewebsite.global.mail.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.tavewebsite.domain.apply.initial.setup.entity.ApplyInitialSetup;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SESMailService {

    @Value("${cloud.aws.ses.username}")
    private String mail;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonSimpleEmailService emailService;

    // 신규 회원 모집 오픈 이메일 전송
    public void sendApplyNotification(String recipient, ApplyInitialSetup applyInitialSetup) {
        try {

            String documentRecruitDate = formatRecruitPeriod(applyInitialSetup.getDocumentRecruitStartDate()
                    , applyInitialSetup.getDocumentRecruitEndDate());

            String documentAnnouncementDate = formatKoreanDateTime(applyInitialSetup.getDocumentAnnouncementDate());

            String interviewDate = formatRecruitPeriod(applyInitialSetup.getInterviewStartDate()
                    , applyInitialSetup.getInterviewEndDate());

            String lastAnnouncementDate = formatKoreanDateTime(applyInitialSetup.getLastAnnouncementDate());

            // 템플릿 변수 매핑
            Map<String, String> templateData = new HashMap<>();
            templateData.put("generation", String.valueOf(applyInitialSetup.getGeneration()));
            templateData.put("documentRecruitDate", documentRecruitDate);
            templateData.put("documentAnnouncementDate", documentAnnouncementDate);
            templateData.put("interviewDate", interviewDate);
            templateData.put("lastAnnouncementDate", lastAnnouncementDate);

            // JSON 문자열로 변환
            String jsonData = objectMapper.writeValueAsString(templateData);

            // 이메일 요청 생성
            SendTemplatedEmailRequest request = new SendTemplatedEmailRequest()
                    .withSource(mail)
                    .withDestination(new Destination().withToAddresses(recipient))
                    .withTemplate("ApplyRecruitTemplate")
                    .withTemplateData(jsonData);

            emailService.sendTemplatedEmail(request);

        } catch (Exception e) {
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }

    private String formatRecruitPeriod(LocalDateTime start, LocalDateTime end) {
        String startFormatted = formatKoreanDateTime(start);
        String endFormatted = formatKoreanDateTime(end);

        return startFormatted + " ~ " + endFormatted;
    }

    private String formatKoreanDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일 (E) HH:mm")
                .withLocale(Locale.KOREAN);
        return dateTime.format(formatter);
    }

}
