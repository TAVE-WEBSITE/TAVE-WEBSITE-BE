package com.tave.tavewebsite.global.mail;


import com.tave.tavewebsite.global.mail.dto.MailResponseDto;
import com.tave.tavewebsite.global.mail.exception.FailCreateMailException;
import com.tave.tavewebsite.global.mail.exception.FailMailSendException;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;
    private final String SUCCESS_SIGN_UP = "[TAVE 관리자 홈페이지] 가입 신청 완료";

    @Value("${spring.mail.username}")
    private String from;

    public MailResponseDto sendManagerRegisterMessage(String to){

        MimeMessage message;
        try {
            message = creatManagerRegisterMessage(to); // "to" 로 관리자 회원가입 신청메일 발송
            emailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new FailCreateMailException(); // message를 생성하는 경우에 발생한 예외 (서버 문제)
        } catch (FailMailSendException e) {
            throw new FailMailSendException(); // Mail을 발송할때 생긴 예외
        }

        return new MailResponseDto(to + " 관리자 회원가입 신청 완료");
    }

    // 메일 내용 작성
    private MimeMessage creatManagerRegisterMessage(String to) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, to);
        // 이메일 제목
        String msgg = ManagerSingUpHtml(message, SUCCESS_SIGN_UP);

        message.setText(msgg, "utf-8", "html");
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(from);

        return message;
    }

    private String ManagerSingUpHtml(MimeMessage message, String subject) throws MessagingException {
        message.setSubject(subject);

        String msgg = "";
        msgg += "<h1>안녕하세요</h1>";
        msgg += "<h1>4차 산업혁명 동아리 TAVE 입니다.</h1>";
        msgg += "<br>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black'>";
        msgg += "<h3 style='color:blue'>관리자 회원가입 신청이 완료됐습니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "</div>";
        return msgg;
    }


}
