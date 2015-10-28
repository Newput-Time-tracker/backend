package com.newput.utility;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
//@PropertySource("classpath:Tracker.properties")
public class EMailSender {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Employee emp;

	@Autowired
	private JsonResService jsonResService;

//	@Value("${Tracker.ip}")
//	private String defaultIp;

	/**
	 * Description : Use to send the verification mail to the user for
	 * registration and password reset.
	 * 
	 * @param module
	 *            - password or registration
	 */
	public String sendMail(String module) {
   try{
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(emp.getEmail());
		email.setSubject("Confirmation Mail");
		if (module.equalsIgnoreCase("registration")) {
			email.setText("Welcome, You are successfully register Please click here http://" + System.getenv("App_Ip")
					+ "/tracker/login?ET=" + emp.getvToken());
		} else if (module.equalsIgnoreCase("password")) {
			email.setText("Welcome, Please confirm your mail id. click here http://tracker/login?PT=" + emp.getpToken()
					+ "&id=" + emp.getId());
		}
		mailSender.send(email);
		return null;
   }catch(Exception ex){
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
	public void sendExcelSheet(String email, File file) {
		MimeMessage message = mailSender.createMimeMessage();
		if (email != null && !email.equalsIgnoreCase("")) {
			try {
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				helper.setTo(email);
				helper.setSubject("Your Time Sheet");
				helper.setText("This is your time sheet please check it.");
				FileSystemResource fileNew = new FileSystemResource(file.getPath());
				helper.addAttachment("Time_Sheet.xls", fileNew);
			} catch (MessagingException e) {
				throw new MailParseException(e);
			}
			mailSender.send(message);
			jsonResService.setDataValue("Your time sheet succefully send to your registered mail id.", "");
		} else {
			jsonResService.errorResponse("Your mail id is not valid.");
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
		email.setText("Welcome, Please fill your daily status for today.Please ignore if already filled.");
		mailSender.send(email);
	}
}
