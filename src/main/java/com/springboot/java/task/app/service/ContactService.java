package com.springboot.java.task.app.service;

import com.springboot.java.task.app.model.Contact;
import com.springboot.java.task.app.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    public void save(Contact contact) {
        contactRepository.save(contact);
    }
}
