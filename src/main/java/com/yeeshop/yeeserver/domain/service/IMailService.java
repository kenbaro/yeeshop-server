package com.yeeshop.yeeserver.domain.service;

import java.io.IOException;

import com.yeeshop.yeeserver.domain.model.MailInfo;

import jakarta.mail.MessagingException;

public interface IMailService {

	void run();

	void queue(String to, String subject, String body);

	void queue(MailInfo mail);

	void send(MailInfo mail) throws MessagingException, IOException;
}
