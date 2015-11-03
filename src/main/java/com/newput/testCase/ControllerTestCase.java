package com.newput.testCase;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.newput.domain.Employee;
import com.newput.mapper.DateSheetMapper;
import com.newput.mapper.EmployeeMapper;
import com.newput.service.EmpService;
import com.newput.service.LoginService;
import com.newput.service.TSchedualService;
import com.newput.utility.EMailSender;
import com.newput.utility.ExcelTimeSheet;
import com.newput.utility.JsonResService;
import com.newput.utility.ReqParseService;
import com.newput.utility.TTUtil;

import static org.junit.Assert.*;
import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class ControllerTestCase {

	@Autowired
	DateSheetMapper dateSheetMapper;

	@Autowired
	private Employee emp;
	
	@Autowired
	private EmpService empService;
	
	@Autowired
	private TSchedualService timeSchedual;

	@Autowired
	private ReqParseService reqParser;

	@Autowired
	private JsonResService jsonResService;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private TTUtil util;
	
	@Autowired
	private EMailSender emailSend;
	
	@Autowired
	private ExcelTimeSheet excelTimeSheet;
	
	@Autowired
	EmployeeMapper empMapper;
	
	
	public Long getCurrentTime() {
		return System.currentTimeMillis() / 1000;
	}

	String email = "rahul@newput.com";
	String password = "abcd";
	String empId = "1";
	String firstName = "deepti";
	String lastName = "gmail";
	String dob = "28-05-1990";
	String doj = "10-10-2015";
	String address = "indore local";
	String contact = "1234567890";
	String gender = "f";
	String month = "October";
	String year = "2015";
	
	
	@Test
	//@Ignore
	public void testRegisterUser(){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			emp.setFirstName(firstName);
			emp.setLastName(lastName);
			emp.setEmail(email);		
			Date userDob = sdf.parse(dob);
			Date userDoj = sdf.parse(doj);		
			emp.setDob(userDob);
			emp.setDoj(userDoj);
			emp.setAddress(address);
			emp.setContact(contact);
			emp.setGender(gender);
			String getPassword = util.md5(password);
			emp.setPassword(getPassword);
			emp.setStatus(false);
			emp.setPasswordVerification(false);
			emp.setRole("guest");
			emp.setCreated(getCurrentTime());
			emp.setTimeZone(new BigDecimal("5.5"));
			String token = util.generateRandomString();		
			emp.setvToken(token);			
		}catch(Exception e){}
		empService.addUser(emp);
		assertEquals(true, jsonResService.isSuccess());
		if (jsonResService.isSuccess()) {
			String sendMail = emailSend.sendMail("registration");
			assertEquals(null, sendMail);
		}
	}
	
	@Test
	//@Ignore
	public void testMailVerification(){
		emp.setvToken("3686");
		emp.setEmail(email);
		empService.mailVerify(emp);
		assertEquals(true, jsonResService.isSuccess());
	}
	
	@Test	
	//@Ignore
	public void testLogin() { 
		emp.setEmail(email);
		emp.setPassword(password);
		loginService.createSession(emp);
		assertEquals(true, jsonResService.isSuccess());
	}	

	@Test
	//@Ignore
	public void testTimeEntry() throws ParseException {
		timeSchedual.timeSheetValue("12:00", "9:00", "19:30", "06-10-2015", "12:30", "21:00", "23:00", Integer.parseInt(empId));
		reqParser.setDateSheetValue("this is my 6 date", "06-10-2015", Integer.parseInt(empId));
		timeSchedual.dateSheetValue();
		assertEquals(true, jsonResService.isSuccess());
	}
	
	@Test
//	@Ignore
	public void testForgotPwd(){
		emp.setEmail(email);
		String ptoken = util.generateRandomString();
		empService.resetPassword(email, ptoken, "password");
		assertEquals(true, jsonResService.isSuccess());
		if (jsonResService.isSuccess()) {
			String sendMail = emailSend.sendMail("password");
			assertEquals(null, sendMail);
		}
	}
	
	@Test
//	@Ignore
	public void testExcelExport(){
		assertEquals(true, util.validCheck(month, year));
		File file = excelTimeSheet.createExcelSheet(Integer.parseInt(empId), month, year);
		assertEquals(true, jsonResService.isSuccess());
		file.delete();
	}
	
	@Test
//	@Ignore
	public void testPasswordVerification(){
		emp.setId(Integer.parseInt(empId));
		emp.setpToken("4669");
		String newPassword = util.md5("rahul");
		emp.setPassword(newPassword);
		emp.setUpdated(getCurrentTime());
		empService.pwdVerify(emp);
		assertEquals(true, jsonResService.isSuccess());
	}
	
	@Test
	//@Ignore
	public void testMonthlyExcel(){
		assertEquals(true, util.validCheck(month, year));
		//excelTimeSheet.getTimeSheetData(month, Integer.parseInt(empId), year);
		assertEquals(true, jsonResService.isSuccess());
		assertNotNull(jsonResService.getData());		
	}			
}
