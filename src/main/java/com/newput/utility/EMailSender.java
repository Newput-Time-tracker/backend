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
import org.springframework.mail.SimpleMailMessage;
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
			VelocityContext context = new VelocityContext();
			context.put("FirstName", emp.getFirstName());
			context.put("url", SystemConfig.get("WEBAPP_URL"));

			final StringWriter writer = new StringWriter();

			if (module.equalsIgnoreCase("registration")) {
				context.put("Id", emp.getEmail());
				context.put("token", emp.getvToken());
				context.put("webUrl", "app/verifyuser?ET=");
				context.put("param", "&EM=");
				context.put("msg", "click here");
				Template t = velocityEngine.getTemplate("templates/MailVerification.vm");

				t.merge(context, writer);
			} else if (module.equalsIgnoreCase("password")) {
				context.put("Id", emp.getId());
				context.put("token", emp.getpToken());
				context.put("webUrl", "app/resetpassword?PT=");
				context.put("param", "&ID=");
				Template t = velocityEngine.getTemplate("templates/MailVerification.vm");

				t.merge(context, writer);
			}
			mailSender.send(setMimeTypeContent(writer.toString()));

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

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(emp.getEmail());
		email.setSubject("Notification Mail");
		email.setText("Welcome, Please fill your daily status for today. Please ignore if already filled.");
		mailSender.send(email);
	}

	public MimeMessagePreparator setMimeTypeContent(String body) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {

				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
				message.setTo(emp.getEmail());
				message.setFrom(new InternetAddress(SystemConfig.get("MAIL_USERID")));
				message.setSubject("Confirmation Mail");
				message.setText(body, true);
			}
		};
		return preparator;
	}
}
