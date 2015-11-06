package com.newput.utility;

import java.io.File;
import java.io.StringWriter;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.newput.domain.Employee;

/**
 * {@link}
 * 
 * @author Newput Description : Method use to send email to the registered mail
 *         id for the verification in this method mailSender variable
 *         is @Autowired with bean defined in the applicationContext.xml
 */
@Service
public class EMailSender {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Employee emp;

	@Autowired
	private JsonResService jsonResService;

	@Autowired
	private VelocityEngine velocityEngine;

	/**
	 * Description : Use to send the verification mail to the user for
	 * registration and password reset.
	 * 
	 * @param module
	 *            - password or registration
	 */
	public String sendMail(String module) {
		try {
			String subject = "subject";
			VelocityContext context = new VelocityContext();
			String fName = Character.toUpperCase(emp.getFirstName().charAt(0)) + emp.getFirstName().substring(1);
			context.put("FirstName", fName);
			context.put("url", SystemConfig.get("WEBAPP_URL"));

			final StringWriter writer = new StringWriter();

			if (module.equalsIgnoreCase("registration")) {
				subject = "Account activation";
				context.put("Id", emp.getEmail());
				context.put("token", emp.getvToken());
				context.put("webUrl", "verifyuser?ET=");
				context.put("param", "&EM=");
				context.put("msg",
						"To be able to sign in to your account, please verify your email address first by clicking the following link:");
				context.put("msg1", "");
				context.put("msg2", "");
				Template t = velocityEngine.getTemplate("templates/MailVerification.vm");

				t.merge(context, writer);
			} else if (module.equalsIgnoreCase("password")) {
				subject = "Reset your time-tracker account password";
				context.put("Id", emp.getId());
				context.put("token", emp.getpToken());
				context.put("webUrl", "resetpassword?PT=");
				context.put("param", "&ID=");
				context.put("msg",
						"We have received a request to reset the password of your account. If you made this request, please ");
				context.put("msg1", "The password reset link is valid for 48 hour.");
				context.put("msg2", "If you didn't raise this request, please ignore this email.");
				Template t = velocityEngine.getTemplate("templates/MailVerification.vm");

				t.merge(context, writer);
			}
			mailSender.send(setMimeTypeContent(writer.toString(), subject));

			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Email id is not valid to send email.";
		}
	}

	/**
	 * Description : Use to send the time sheet on the registered mail id.
	 * 
	 * @param email
	 *            - abc.xyz@newput.com
	 * @param file
	 *            - excelFile
	 */
	public String sendExcelSheet(String email, File file, String sheetName) {
		if (email != null && !email.equalsIgnoreCase("")) {
			try {
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				helper.setTo(email);
				helper.setSubject("Your Time Sheet");
				helper.setText("This is your time sheet please check it.");
				FileSystemResource fileNew = new FileSystemResource(file.getPath());
				helper.addAttachment(sheetName, fileNew);
				mailSender.send(message);
				jsonResService.setDataValue("Your time sheet succefully send to your registered mail id.", "");
				return null;
			} catch (MessagingException e) {
				return "Your mail is not send please retry";
			}
		} else {
			jsonResService.errorResponse("Your mail id is not valid.");
			return "Your mail id is not valid.";
		}
	}

	/**
	 * Description : Use to send the reminder notification to user to fill the
	 * time sheet.
	 * 
	 * @param emp
	 *            - An Object
	 */
	public void notificationMail(Employee emp) {

		final StringWriter writer = new StringWriter();
		String subject = "Reminder to fill time-sheet";
		VelocityContext context = new VelocityContext();

		context.put("url", SystemConfig.get("WEBAPP_URL"));
		context.put("msg",
				"Hi, Please fill your time-sheet. You can login to time-tracker by clicking the following link:");
		context.put("webUrl", "login");
		Template t = velocityEngine.getTemplate("templates/Notification.vm");
		t.merge(context, writer);

		mailSender.send(setMimeTypeContent(writer.toString(), subject));
	}

	public MimeMessagePreparator setMimeTypeContent(String body, String subject) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {

				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
				message.setTo(emp.getEmail());
				message.setFrom(new InternetAddress(SystemConfig.get("MAIL_USERID")));
				message.setSubject(subject);
				message.setText(body, true);
			}
		};
		return preparator;
	}
}
