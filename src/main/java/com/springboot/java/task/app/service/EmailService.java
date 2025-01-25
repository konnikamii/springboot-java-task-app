package com.springboot.java.task.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    String yourEmail = "your-email@example.com";

    public void sendEmail(String name, String email, String subject, String message) {
        // Format the message
        String formattedMessage = String.format(
                "Name: %s\nEmail: %s\n\nMessage:\n%s",
                name, email, message);
        SimpleMailMessage new_message = new SimpleMailMessage();

        new_message.setFrom(email);
        new_message.setTo(yourEmail);
        new_message.setSubject(subject);
        new_message.setText(formattedMessage);
        mailSender.send(new_message);
    }
}
