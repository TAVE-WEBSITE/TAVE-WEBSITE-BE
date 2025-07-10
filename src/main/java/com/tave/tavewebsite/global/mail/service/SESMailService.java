package com.tave.tavewebsite.global.mail.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.tave.tavewebsite.domain.apply.initial.setup.entity.ApplyInitialSetup;
import com.tave.tavewebsite.domain.member.entity.DepartmentType;
import com.tave.tavewebsite.global.common.FieldType;
import com.tave.tavewebsite.global.mail.util.SESMailFormatUtil;
import com.tave.tavewebsite.global.mail.util.SESTemplateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SESMailService {

    private final SESTemplateUtil templateUtil;
    private final SESMailFormatUtil mailFormatUtil;
    private final AmazonSimpleEmailService emailService;

    // 이메일 인증번호 발송
    public void sendEmailAuthenticationCode(String recipient, String authenticationCode) {
        try {
            Map<String, String> templateData = new HashMap<>();
            templateData.put("authCode", authenticationCode);

            SendTemplatedEmailRequest request = templateUtil.createTemplatedEmailRequest(
                    recipient, "EmailVerificationTemplate", templateData
            );
            emailService.sendTemplatedEmail(request);
        } catch (Exception e) {
            throw new RuntimeException("인증 번호 전송 실패", e);
        }
    }

    // 관리자 가입 거절 안내 메일 전송
    public void sendAdminRejectMail(String recipient, String memberName) {
        try {
            Map<String, String> templateData = new HashMap<>();
            templateData.put("memberName", memberName);

            SendTemplatedEmailRequest request = templateUtil.createTemplatedEmailRequest(
                    recipient, "AdminSignupRejectTemplate", templateData
            );
            emailService.sendTemplatedEmail(request);

        } catch (Exception e) {
            throw new RuntimeException("관리자 가입 거절 메일 전송 실패", e);
        }
    }


    // 관리자 가입 승인 안내 메일 전송
    public void sendAdminApprovalMail(String recipient, String memberName, DepartmentType departmentType) {
        try {
            Map<String, String> templateData = new HashMap<>();
            templateData.put("name", memberName);
            templateData.put("departmentType", departmentType.name());

            SendTemplatedEmailRequest request = templateUtil.createTemplatedEmailRequest(
                    recipient, "AdminSignupApprovalTemplate", templateData
            );
            emailService.sendTemplatedEmail(request);

        } catch (Exception e) {
            throw new RuntimeException("관리자 가입 승인 메일 전송 실패", e);
        }
    }


    // 지원 완료 이메일 전송
    public void sendApplySubmitMail(String recipient, String memberName, String generation, FieldType fieldType,
                                    LocalDateTime nowDateTime, LocalDateTime documentAnnouncementDate) {
        try {
            Map<String, String> templateData = new HashMap<>();
            templateData.put("generation", generation);
            templateData.put("memberName", memberName);
            templateData.put("fieldType", fieldType.getDisplayName());
            templateData.put("nowDateTime", mailFormatUtil.formatKoreanDateTime(nowDateTime));
            templateData.put("documentAnnouncementDate", mailFormatUtil.formatKoreanDateTime(documentAnnouncementDate));

            SendTemplatedEmailRequest request = templateUtil.createTemplatedEmailRequest(
                    recipient, "ApplySubmitTemplate", templateData
            );
            emailService.sendTemplatedEmail(request);

        } catch (Exception e) {
            throw new RuntimeException("지원 완료 메일 전송 실패", e);
        }
    }

    // 최종 결과 발표 이메일 전송
    public void sendFinalResultMail(String recipient, String memberName, String generation) {
        try {
            Map<String, String> templateData = new HashMap<>();
            templateData.put("generation", generation);
            templateData.put("memberName", memberName);

            SendTemplatedEmailRequest request = templateUtil.createTemplatedEmailRequest(
                    recipient, "LastResultTemplate", templateData
            );
            emailService.sendTemplatedEmail(request);

        } catch (Exception e) {
            throw new RuntimeException("최종 결과 메일 전송 실패", e);
        }
    }

    // 서류 평가 완료 메일
    public void sendDocumentResultMail(String recipient, String memberName, String generation) {
        try {
            Map<String, String> templateData = new HashMap<>();
            templateData.put("generation", generation);
            templateData.put("memberName", memberName);

            SendTemplatedEmailRequest request = templateUtil.createTemplatedEmailRequest(
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
            templateData.put("documentRecruitDate", mailFormatUtil.formatRecruitPeriod(
                    applyInitialSetup.getDocumentRecruitStartDate(),
                    applyInitialSetup.getDocumentRecruitEndDate()
            ));
            templateData.put("documentAnnouncementDate",
                    mailFormatUtil.formatKoreanDateTime(applyInitialSetup.getDocumentAnnouncementDate()));
            templateData.put("interviewDate", mailFormatUtil.formatRecruitPeriod(
                    applyInitialSetup.getInterviewStartDate(),
                    applyInitialSetup.getInterviewEndDate()
            ));
            templateData.put("lastAnnouncementDate",
                    mailFormatUtil.formatKoreanDateTime(applyInitialSetup.getLastAnnouncementDate()));

            SendTemplatedEmailRequest request = templateUtil.createTemplatedEmailRequest(
                    recipient, "ApplyRecruitTemplate", templateData
            );
            emailService.sendTemplatedEmail(request);

        } catch (Exception e) {
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }

    // 가입 완료 메일 템플릿
    public void sendJoinSuccessNotification(String recipient, String memberName, String memberEmail) {
        try {
            Map<String, String> templateData = new HashMap<>();
            templateData.put("memberName", memberName);
            templateData.put("memberEmail", memberEmail);

            SendTemplatedEmailRequest request = templateUtil.createTemplatedEmailRequest(
                    recipient, "JoinSuccessTemplate", templateData
            );
            emailService.sendTemplatedEmail(request);
        } catch (Exception e) {
            throw new RuntimeException("회원가입 완료 이메일 전송 실패", e);
        }
    }

    // 관리자 가입 신청 완료 메일
    public void sendAdminApplySuccessNotification(String recipient, String memberName) {
        try {
            Map<String, String> templateData = new HashMap<>();
            templateData.put("memberName", memberName);
            SendTemplatedEmailRequest request = templateUtil.createTemplatedEmailRequest(
                    recipient, "AdminApplySuccessTemplate", templateData
            );
            emailService.sendTemplatedEmail(request);
        } catch (Exception e) {
            throw new RuntimeException("관리자 가입 신청 실패", e);
        }
    }

    // 관리자 이메일 인증번호 발송
    public void sendAdminEmailVerification(String recipient, String authenticationCode) {
        try {
            Map<String, String> templateData = new HashMap<>();
            templateData.put("authCode", authenticationCode);

            SendTemplatedEmailRequest request = templateUtil.createTemplatedEmailRequest(
                    recipient, "AdminEmailVerificationTemplate", templateData
            );
            emailService.sendTemplatedEmail(request);
        } catch (Exception e) {
            throw new RuntimeException("관리자 이메일 인증번호 전송 실패", e);
        }
    }

    // 일반 유저 이메일 인증번호 발송
    public void sendUserEmailVerification(String recipient, String authenticationCode) {
        try {
            Map<String, String> templateData = new HashMap<>();
            templateData.put("authCode", authenticationCode);

            SendTemplatedEmailRequest request = templateUtil.createTemplatedEmailRequest(
                    recipient, "UserEmailVerificationTemplate", templateData
            );
            emailService.sendTemplatedEmail(request);
        } catch (Exception e) {
            throw new RuntimeException("유저 이메일 인증번호 전송 실패", e);
        }
    }

}
