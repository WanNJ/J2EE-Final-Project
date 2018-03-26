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
        orgCodeEmail.setSubject("Online Education Organization Registration");
        orgCodeEmail.setText("Hi, " + organization.getName() + "! Thank you for your choice of our online education platform." +
                "The code of your organization is:\n" + organization.getOrgCode() + "\nOur college manager will check your application ASAP. " +
                "Once you are approved, you will receive another email, until which your account will not be able to login. Thank you for your patience!");

        mailSender.send(orgCodeEmail);
    }

    @Async
    public void declineApplication(Organization organization) {
        SimpleMailMessage email = new SimpleMailMessage();

        email.setFrom(from);
        email.setTo(organization.getEmail());
        email.setSubject("Application Declined!");
        email.setText("We're very sorry to inform you that your application for , " + organization.getName() + " has been declined! Thank you for your choice of our online education platform." +
                "FYI, the code of your organization is:\n" + organization.getOrgCode());

        mailSender.send(email);
    }

    @Async
    public void approveApplication(Organization organization) {
        SimpleMailMessage email = new SimpleMailMessage();

        email.setFrom(from);
        email.setTo(organization.getEmail());
        email.setSubject("Application Approved!");
        email.setText("Congratulations, " + organization.getName() + "! Your application has been approved! You can now login to your account(" + organization.getOrgCode() + ") through our system!");
        mailSender.send(email);
    }

    @Async
    public void declineInfoChange(Organization organization) {
        SimpleMailMessage email = new SimpleMailMessage();

        email.setFrom(from);
        email.setTo(organization.getEmail());
        email.setSubject("Change to Organization Information Declined!");
        email.setText("Sorry, your organization(" + organization.getOrgCode() + ")'s information change application has been declined!");
        mailSender.send(email);
    }

    @SuppressWarnings("Duplicates")
    @Async
    public void approveInfoChange(Organization organization) {
	    SimpleMailMessage email = new SimpleMailMessage();

	    email.setFrom(from);
	    email.setTo(organization.getEmail());
	    email.setSubject("Change to Organization Information Approved!");
	    email.setText("Congratulations, your organization(" + organization.getOrgCode() + ")'s information has just been updated, please login to your account to check!");
	    mailSender.send(email);
    }
}
