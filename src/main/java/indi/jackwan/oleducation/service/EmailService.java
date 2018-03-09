package indi.jackwan.oleducation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailService {

	private JavaMailSender mailSender;
	
	@Autowired
	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	// The calling code doesn't have to wait for the send operation to complete in order to continue
	@Async
	public void sendEmail(SimpleMailMessage email) {
		mailSender.send(email);
	}
}
