package com.efioco.ticketsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	private static final Logger logger = LoggerFactory.getLogger(MailService.class);
	
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimple(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
        logger.info("flusso invio mail completato");
    }
}
