package com.tave.tavewebsite.global.mail.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.tave.tavewebsite.global.mail.exception.FailCreateMailException;
import com.tave.tavewebsite.global.mail.exception.FailMailSendException;
import com.tave.tavewebsite.global.mail.util.SESMailUtil;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SESMailService {

    private final SESMailUtil sesMailUtil;
    private final AmazonSimpleEmailService emailService;

    public void sendApplyNotification(String recipient) {
        try {
            SendRawEmailRequest request = sesMailUtil.getSendRawEmailRequest(
                    "TAVE 4기 모집 안내",
                    "TAVE 4기 모집이 시작되었습니다. 자세한 내용은 이메일 본문을 확인해주세요.",
                    recipient,
                    getApplyNotificationTemplate()
            );
            emailService.sendRawEmail(request);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new FailCreateMailException(); // message를 생성하는 경우에 발생한 예외 (서버 문제)
        } catch (FailMailSendException e) {
            throw new FailMailSendException(); // Mail을 발송할때 생긴 예외
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getApplyNotificationTemplate() {
        return """
                <!DOCTYPE html>
                       <html lang="ko">
                       <head>
                         <meta charset="UTF-8" />
                         <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                         <title>TAVE 신규 회원 모집</title>
                       </head>
                       <body style="font-family: 'Helvetica Neue', Arial, sans-serif; background-color: #f4f9fc; margin: 0; padding: 0;">
                         <div style="max-width: 600px; margin: 30px auto; background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); overflow: hidden;">
                           <div style="background-color: #0077cc; color: white; text-align: center; padding: 30px 20px;">
                             <h1 style="margin: 0; font-size: 26px;">TAVE 4기 신규 회원 모집 시작!</h1>
                           </div>
                           <div style="padding: 30px 25px; color: #333333; line-height: 1.6;">
                             <h2 style="color: #0077cc; margin-top: 0;">4차 산업혁명, TAVE와 함께하세요</h2>
                             <p>
                               TAVE는 기술 기반의 미래를 만들어가는 4차 산업혁명 동아리입니다.<br/>
                               AI, 웹 개발, 데이터 분석 등 다양한 분야에 관심 있는 열정적인 여러분을 기다립니다.
                             </p>
                             <p>
                               📅 모집 기간: <strong>2025년 6월 1일 ~ 6월 10일</strong><br/>
                               🧠 대상: 개발/기획/디자인 등 IT에 관심 있는 누구나<br/>
                               🎯 활동: 프로젝트 중심 팀 활동, 기술 세미나, 멘토링
                             </p>
                             <p style="text-align: center; margin-top: 20px;">
                               <a href="https://www.tave-wave.com" target="_blank" style="display: inline-block; background-color: #0077cc; color: white; padding: 12px 24px; text-decoration: none; border-radius: 6px;">지금 바로 지원하기</a>
                             </p>
                           </div>
                         </div>
                       </body>
                       </html>
                
                """;
    }

}
