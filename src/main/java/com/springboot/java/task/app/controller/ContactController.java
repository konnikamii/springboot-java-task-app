package com.springboot.java.task.app.controller;

import com.springboot.java.task.app.model.Contact;
import com.springboot.java.task.app.service.ContactService;
import com.springboot.java.task.app.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/contact/")
    public String register(@RequestParam("name") String name,
                           @RequestParam("email") String email,
                           @RequestParam("subject") String subject,
                           @RequestParam("message") String message) {
        Contact contact = Contact.builder().name(name).email(email).subject(subject).message(message).build();
        contactService.save(contact);
        // Send email to MailHog
        try {
            emailService.sendEmail(name, email, subject, message);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "Contact form saved";
        }
        return "Contact form submitted";
    }
}
