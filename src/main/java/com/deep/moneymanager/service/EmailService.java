package com.deep.moneymanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${{spring.mail.username}")  // better than smtp.from
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) {

        System.out.println("📧 Sending email...");
        System.out.println("FROM: " + fromEmail);
        System.out.println("TO: " + to);

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            // hard-safe fallback
            message.setFrom(fromEmail != null ? fromEmail : "samantasurajit533@gmail.com");

            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            System.out.println("✅ Email sent successfully!");

        } catch (MailAuthenticationException e) {
            System.out.println("❌ AUTH ERROR: Check username or app password");
            e.printStackTrace();

        } catch (Exception e) {
            System.out.println("❌ GENERAL EMAIL ERROR");
            e.printStackTrace();
        }
    }
}