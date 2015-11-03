package com.newput.testCase;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.newput.domain.DateSheet;
import com.newput.domain.Employee;
import com.newput.domain.EmployeeExample;
import com.newput.domain.Session;
import com.newput.domain.SessionExample;
import com.newput.domain.TimeSheet;
import com.newput.mapper.DateSheetMapper;
import com.newput.mapper.EmployeeMapper;
import com.newput.mapper.SessionMapper;
import com.newput.service.EmpService;
import com.newput.service.TSchedualService;
import com.newput.utility.EMailSender;
import com.newput.utility.ExcelTimeSheet;
import com.newput.utility.JsonResService;
import com.newput.utility.ReqParseService;
import com.newput.utility.TTUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class ServiceTestCase {

	@Autowired
	private DateSheet dateSheet;

	@Autowired
	DateSheetMapper dateSheetMapper;

	@Autowired
	private TimeSheet timeSheet;

	@Autowired
	private Session session;

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
	private TTUtil util;

	@Autowired
	private EMailSender emailSend;

	@Autowired
	private ExcelTimeSheet excelTimeSheet;

	@Autowired
	EmployeeMapper empMapper;

	@Autowired
	private SessionMapper sessionMapper;

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
	// @Ignore
	public void testAddUser() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date userDob = (Date) sdf.parse("10-10-1990");
		Date userDoj = (Date) sdf.parse("10-10-2015");
		emp.setAddress("Indore");
		emp.setContact("0123456789");
		emp.setCreated(1446091979L);
		emp.setDob(userDob);
		emp.setDoj(userDoj);
		emp.setEmail("abc@newput.com");
		emp.setFirstName("abc");
		emp.setGender("f");
		emp.setLastName("xyz");
		emp.setPassword("vuhduhuhd");
		emp.setvToken("8569");
		int i = empMapper.insertSelective(emp);
		assertEquals(true, i > 0);
	}

	@Test
	// @Ignore
	public void testMailVerify() {
		emp.setEmail("abc@newput.com");
		EmployeeExample example = new EmployeeExample();
		example.createCriteria().andEmailEqualTo(emp.getEmail());
		emp.setStatus(true);
		emp.setUpdated(new Date().getTime() / 1000);
		emp.setRole("employee");
		int i = empMapper.updateByExampleSelective(emp, example);
		assertEquals(true, i > 0);
	}

	@Test
	// @Ignore
	public void testCreateSession() {
		emp.setId(34);
		emp.setFirstName("abc");
		emp.setEmail("abc@newput.com");
		session.setEmpId(emp.getId());
		session.setEmpName(emp.getFirstName());
		session.setToken(util.createSessionKey(getCurrentTime(), emp.getEmail()));
		session.setCreated(getCurrentTime());
		session.setExpiresWhen(getCurrentTime() + 3600);
		int i = sessionMapper.insertSelective(session);
		assertEquals(true, i > 0);

	}

	@Test
	// @Ignore
	public void testSignOut() {
		SessionExample sessionExample = new SessionExample();
		sessionExample.createCriteria().andEmpIdEqualTo(34);
		List<Session> sessionList = sessionMapper.selectByExample(sessionExample);
		Session localSession = sessionList.get(0);
		localSession.setExpiresWhen(getCurrentTime());
		int i = sessionMapper.updateByExample(localSession, sessionExample);
		assertEquals(true, i > 0);
	}

	@Test
	// @Ignore
	public void testTimeSheetValue() {
		timeSheet = reqParser.setTimeSheetValue("25-10-2015", "9:00", "19:00", "1", 34);
		boolean status = timeSchedual.saveTimeSheet(timeSheet);
		assertEquals(true, status);
	}

	@Test
	// @Ignore
	public void testDateSheetValue() {
		dateSheet.setCreated(getCurrentTime());
		dateSheet.setEmpId(34);
		dateSheet.setWorkDate(getCurrentTime());
		dateSheet.setWorkDesc("Test case entry");

		int j = dateSheetMapper.insertSelective(dateSheet);
		assertEquals(true, j > 0);
	}

	@Test
	// @Ignore
	public void testResetPassword() {
		emp.setEmail("abc@newput.com");
		EmployeeExample example = new EmployeeExample();
		example.createCriteria().andEmailEqualTo(emp.getEmail());
		List<Employee> empl = empMapper.selectByExample(example);
		Employee emply = empl.get(0);
		emply.setpToken("5869");
		emply.setpExpireAt(getCurrentTime() + 30);
		emply.setUpdated(getCurrentTime());
		int i = empMapper.updateByExampleSelective(emply, example);
		assertEquals(true, i > 0);
	}

	@Test
	// @Ignore
	public void testPwdVerify() {
		emp.setId(34);
		emp.setpToken("5869");
		emp.setPassword("abcd");
		int i = 0;

		EmployeeExample example = new EmployeeExample();
		example.createCriteria().andIdEqualTo(emp.getId()).andPTokenEqualTo(emp.getpToken());
		List<Employee> employeeList = empMapper.selectByExample(example);
		System.out.println(employeeList.size());
		if (employeeList.size() > 0) {
			Employee employee = employeeList.get(0);
			if (employee.getpExpireAt() >= getCurrentTime()) {
				example.createCriteria().andIdEqualTo(emp.getId());
				employee.setPassword(emp.getPassword());
				employee.setUpdated(new Date().getTime() / 1000);
				i = empMapper.updateByExampleSelective(employee, example);
				assertEquals(true, i > 0);
			} else {
				assertEquals(true, i = 0);
			}
		} else {
			assertEquals(true, i = 0);
		}
	}

	@Test
	// @Ignore
	public void testMailExcelSheet() {
		assertEquals(true, util.validCheck(month, year));
		File file = excelTimeSheet.createExcelSheet(Integer.parseInt(empId), month, year);
		assertEquals(true, jsonResService.isSuccess());
		assertEquals(null, emailSend.sendExcelSheet(excelTimeSheet.getEmpEmail(Integer.parseInt(empId)), file,
				excelTimeSheet.getTimeSheetName(Integer.parseInt(empId), month, year)));
		file.delete();
	}

	@Test
	// @Ignore
	public void testResendMail() {
		String registrationToken = util.generateRandomString();
		empService.resetPassword(email, registrationToken, "registration");
		assertEquals(true, jsonResService.isSuccess());
		if (jsonResService.isSuccess()) {
			String sendMail = emailSend.sendMail("registration");
			assertEquals(null, sendMail);
		}
		String passwordToken = util.generateRandomString();
		empService.resetPassword(email, passwordToken, "password");
		assertEquals(true, jsonResService.isSuccess());
		if (jsonResService.isSuccess()) {
			String sendMail = emailSend.sendMail("password");
			assertEquals(null, sendMail);
		}
	}
}
