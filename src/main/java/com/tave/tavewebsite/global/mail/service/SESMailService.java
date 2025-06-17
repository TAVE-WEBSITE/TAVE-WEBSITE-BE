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

    // 서류 평가 완료 메일
    public void sendDocumentResultMail(String recipient, String memberName, String generation) {
        try {
            Map<String, String> templateData = new HashMap<>();
            templateData.put("generation", generation);
            templateData.put("memberName", memberName);

            SendTemplatedEmailRequest request = createTemplatedEmailRequest(
                    recipient, "DocumentResultTemplate", templateData
            );
            emailService.sendTemplatedEmail(request);

        } catch (Exception e) {
            throw new RuntimeException("서류 결과 메일 전송 실패", e);
        }
    }

    // 신규 회원 모집 오픈 이메일 전송
    public void sendApplyNotification(String recipient, ApplyInitialSetup applyInitialSetup) {
        try {
            Map<String, String> templateData = new HashMap<>();
            templateData.put("generation", String.valueOf(applyInitialSetup.getGeneration()));
            templateData.put("documentRecruitDate", formatRecruitPeriod(
                    applyInitialSetup.getDocumentRecruitStartDate(),
                    applyInitialSetup.getDocumentRecruitEndDate()
            ));
            templateData.put("documentAnnouncementDate",
                    formatKoreanDateTime(applyInitialSetup.getDocumentAnnouncementDate()));
            templateData.put("interviewDate", formatRecruitPeriod(
                    applyInitialSetup.getInterviewStartDate(),
                    applyInitialSetup.getInterviewEndDate()
            ));
            templateData.put("lastAnnouncementDate", formatKoreanDateTime(applyInitialSetup.getLastAnnouncementDate()));

            SendTemplatedEmailRequest request = createTemplatedEmailRequest(
                    recipient, "ApplyRecruitTemplate", templateData
            );
            emailService.sendTemplatedEmail(request);

        } catch (Exception e) {
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }

    private SendTemplatedEmailRequest createTemplatedEmailRequest(String recipient, String templateName,
                                                                  Map<String, String> templateData)
            throws Exception {

        String jsonData = objectMapper.writeValueAsString(templateData);

        return new SendTemplatedEmailRequest()
                .withSource(mail)
                .withDestination(new Destination().withToAddresses(recipient))
                .withTemplate(templateName)
                .withTemplateData(jsonData);
    }

    private String formatRecruitPeriod(LocalDateTime start, LocalDateTime end) {
        return formatKoreanDateTime(start) + " ~ " + formatKoreanDateTime(end);
    }

    private String formatKoreanDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일 (E) HH:mm")
                .withLocale(Locale.KOREAN);
        return dateTime.format(formatter);
    }
}
