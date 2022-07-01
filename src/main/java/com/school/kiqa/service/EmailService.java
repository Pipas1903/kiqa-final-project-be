package com.school.kiqa.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.school.kiqa.persistence.entity.EmailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void send(EmailMessage emailMessage) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setFrom(emailMessage.getSender());
        helper.setSubject(emailMessage.getSubject());
        helper.setText(emailMessage.getMessage(), true);
        helper.setTo(emailMessage.getReceivers()
                .toArray(new String[emailMessage.getReceivers().size()]));

        javaMailSender.send(mimeMessage);
    }
}
