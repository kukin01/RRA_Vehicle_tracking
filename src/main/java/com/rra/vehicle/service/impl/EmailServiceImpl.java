
package com.rra.vehicle.service.impl;

import com.rra.vehicle.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    @Async
    public void sendVerificationEmail(String to, String name, String verificationCode) {
        try {
            Context context = new Context();
            context.setVariable("userName", name);
            context.setVariable("verificationCode", verificationCode);
            
            String content = templateEngine.process("email/verification", context);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("RRA - Verify Your Email");
            helper.setText(content, true);
            
            mailSender.send(message);
            logger.info("Verification email sent to: {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send verification email to {}: {}", to, e.getMessage());
        }
    }

    @Override
    @Async
    public void sendPasswordResetEmail(String to, String name, String resetCode) {
        try {
            Context context = new Context();
            context.setVariable("userName", name);
            context.setVariable("resetCode", resetCode);
            
            String content = templateEngine.process("email/password-reset", context);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("RRA - Password Reset Request");
            helper.setText(content, true);
            
            mailSender.send(message);
            logger.info("Password reset email sent to: {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send password reset email to {}: {}", to, e.getMessage());
        }
    }
    
    @Override
    @Async
    public void sendSuccessEmail(String to, String name, String message, String details) {
        try {
            Context context = new Context();
            context.setVariable("userName", name);
            context.setVariable("message", message);
            context.setVariable("details", details);
            
            String content = templateEngine.process("email/success-notification", context);
            
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("RRA - " + message);
            helper.setText(content, true);
            
            mailSender.send(mimeMessage);
            logger.info("Success notification email sent to: {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send success email to {}: {}", to, e.getMessage());
        }
    }
}
