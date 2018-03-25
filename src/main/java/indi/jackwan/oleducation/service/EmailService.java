package indi.jackwan.oleducation.service;

import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailService {
	@Value("${app.host.url}")
	private String hostUrl;
    @Value("${spring.mail.username}")
    private String from;
	@Autowired
    private JavaMailSender mailSender;

	// The calling code doesn't have to wait for the send operation to complete in order to continue
	@Async
	public void sendConfirmationEmail(User user) {
        SimpleMailMessage registrationEmail = new SimpleMailMessage();

        String confirmationUrl = hostUrl + "confirmUser?token=" + user.getConfirmationToken();
        // Configure mail parameters.
        registrationEmail.setFrom(from);
        registrationEmail.setTo(user.getEmail());
        registrationEmail.setSubject("Online Education Registration Confirmation");
        registrationEmail.setText("Hi, " + user.getNickname() + "! Thank you for your choice of our service." +
                "To confirm your e-mail address, please click the link below:\n" + confirmationUrl);

		mailSender.send(registrationEmail);
	}

	@Async
    public void sendOrgCode(Organization organization) {
        SimpleMailMessage orgCodeEmail = new SimpleMailMessage();

        orgCodeEmail.setFrom(from);
        orgCodeEmail.setTo(organization.getEmail());
        orgCodeEmail.setSubject("Online Education Organization Registeration");
        orgCodeEmail.setText("Hi, " + organization.getName() + "! Thank you for your choice of our online education platform." +
                "The code of your organization is:\n" + organization.getOrgCode() + "\nOur college manager will check your application ASAP. " +
                "Once you are approved, you will receive another email, until which your account will not be able to login. Thank you for your patience!");

        mailSender.send(orgCodeEmail);
    }
}
