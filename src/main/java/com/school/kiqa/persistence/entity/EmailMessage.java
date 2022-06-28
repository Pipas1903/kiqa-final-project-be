package com.school.kiqa.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmailMessage {

    private String subject;
    private String message;
    private String sender;
    private List<String> receivers;

    public EmailMessage(String subject, String message, String sender, List<String> receivers) {
        this.subject = subject;
        this.message = message;
        this.sender = sender;
        this.receivers = receivers;
    }
}
