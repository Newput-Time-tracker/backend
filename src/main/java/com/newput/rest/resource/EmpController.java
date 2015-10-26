package com.newput.rest.resource;

import java.io.File;
import java.text.SimpleDateFormat;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.newput.domain.Employee;
import com.newput.domain.TrackerException;
import com.newput.service.EmpService;
import com.newput.service.LoginService;
import com.newput.service.SelectiveExcel;
import com.newput.service.TSchedualService;
import com.newput.utility.ExcelTimeSheet;
import com.newput.utility.JsonResService;
import com.newput.utility.ReqParseService;
import com.newput.utility.TTUtil;
import com.newput.utility.EMailSender;

/**
 * Description : Use as a controller class to pass control on the services
 * 
 * @author Newput
 * 
 */
@Controller
@Path("/employee")
public class EmpController {

	@Autowired
	private SelectiveExcel excel;

	@Autowired
	private TSchedualService timeSchedual;

	@Autowired
	private JsonResService jsonResService;

	@Autowired
	private EmpService empService;

	@Autowired
	private Employee emp;

	@Autowired
	private EMailSender emailSend;

	@Autowired
	private ReqParseService reqParser;

	@Autowired
	private LoginService loginService;

	@Autowired
	private TTUtil util;

	@Autowired
	private ExcelTimeSheet excelTimeSheet;

	/**
	 * Required url to redirect :
	 * http://time-tracker-backend-app.herokuapp.com/Tracker/rest/employee/
	 * register
	 * 
	 * @throws TrackerException
	 * @POST Description : Use to add new user into the system and send the
	 *       validation email to the registered mail id {@link EMailSender}
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param dob
	 * @param doj
	 * @param address
	 * @param contact
	 * @param gender
	 * @param password
	 * @return JSONObject Success Response : { response: { data: [1] 0: {
	 *         firstName: "abc" lastName: "xyz" address: "indore" gender: "m"
	 *         dob: 580780800000 contact: "0123456789" id: 1 email:
	 *         "abc.xyz@gmail.com" doj: 1442188800000 }- - success: true rcode:
	 *         null error: null }- }
	 * 
	 *         Fail Response :{ response: { data: [1] 0: { msg: null }- -
	 *         success: false rcode: "505" error: "Email id already registered"
	 *         }- }
	 * 
	 */
	@Path("/register")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject registerUser(@FormParam("firstName") String firstName, @FormParam("lastName") String lastName,
			@FormParam("email") String email, @FormParam("dob") String dob, @FormParam("doj") String doj,
			@FormParam("address") String address, @FormParam("contact") String contact,
			@FormParam("gender") String gender, @FormParam("password") String password) {
		try {
			String token = util.generateRandomString();
			reqParser.setEmployeeValue(firstName, lastName, email, dob, doj, address, contact, gender, password, token);
			empService.addUser(emp);
			if (jsonResService.isSuccess()) {
				emailSend.sendMail("registration");
			}
		} catch (Exception ex) {
			jsonResService.errorResponse(new TrackerException("Email id already registered").getMessage());
		}
		return jsonResService.responseSender();
	}

	/**
	 * Required url to redirect :
	 * http://time-tracker-backend-app.herokuapp.com/Tracker/rest/employee/
	 * verify
	 * 
	 * @POST
	 * 		<p>
	 *       Description : Use to verify the register email of user with respect
	 *       to the generated token provided into the jsp link at the mail id of
	 *       user.
	 * @param emailId
	 * @param token
	 * @return
	 * 		<p>
	 *         success response: { response: { data: [1] 0: { firstName: "abc"
	 *         lastName: "xyz" address: "indore" gender: "m" dob: 612124200000
	 *         contact: "0123456789" id: 2 email: "abc@newput.com" doj:
	 *         1432492200000 }- - success: true rcode: null error: null }- }
	 * 
	 *         <p>
	 *         Fail Response :{ response: { data: [1] 0: { msg: null }- -
	 *         success: false rcode: "505" error: "Email id already registered"
	 *         }- }
	 */
	@Path("/verify")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject mailVerification(@FormParam("email") String emailId, @FormParam("token") String token) {
		try {
			if (emailId != null && !emailId.equalsIgnoreCase("")) {
				if (token != null && !token.equalsIgnoreCase("")) {
					reqParser.setValidationValue(emailId, token);
					empService.mailVerify(emp);
				} else {
					jsonResService.errorResponse("token can not be blank");
				}
			} else {
				jsonResService.errorResponse("Mail id can not be null");
			}
		} catch (Exception ex) {
			jsonResService.errorResponse(new TrackerException("Internal Server Error").getMessage());
		}
		return jsonResService.responseSender();
	}

	/**
	 * @ Required url to redirect :
	 * http://time-tracker-backend-app.herokuapp.com/Tracker/rest/employee/
	 * verify
	 * 
	 * @POST
	 * 		<p>
	 *       Description : Use to login and creating the session for existing
	 *       user
	 * @param email
	 * @param password
	 * 
	 * @return Success Response : { response: { data: [2] 0: { firstName: "abc"
	 *         lastName: "xyz" address: "indore" gender: "f" dob: 59266531800000
	 *         contact: "8871786146" id: 1 email: "xyz@newput.com" doj:
	 *         1439317800000 }- 1: { token: "ACBDE3A9C9956DD10BD3A5BC6C4DF017"
	 *         }- - success: true rcode: null error: null }- }
	 *         <p>
	 *         here token is the session token.
	 * 
	 *         Fail Response :{ response: { data: [1] 0: { msg: null }- -
	 *         success: false rcode: "505" error: "invalid user" }- }
	 * 
	 *         <p>
	 *         it get failed when either user is not registered or either email
	 *         id or password is wrong.
	 */

	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject login(@FormParam("email") String email, @FormParam("password") String password) {
		try {
			if (email != null && !email.equalsIgnoreCase("") && util.mailFormat(email)) {
				if (password != null && !password.equalsIgnoreCase("")) {
					reqParser.setSessionValue(email, password, "");
					loginService.createSession(emp);
				} else {
					jsonResService.errorResponse("password can not be blank");
				}
			} else {
				jsonResService.errorResponse("Mail id can not be null and in proper format");
			}
		} catch (Exception ex) {
			jsonResService.errorResponse(new TrackerException("Internal Server Error ").getMessage());
		}
		return jsonResService.responseSender();
	}

	/**
	 * @ Required url to redirect :
	 * http://time-tracker-backend-app.herokuapp.com/Tracker/rest/employee
	 * /timeEntry
	 * 
	 * @POST
	 * 		<p>
	 *       Description : To enter or update the time value in time sheet.
	 * @param lunchIn
	 * @param in
	 * @param out
	 * @param workdate
	 * @param lunchOut
	 * @param nightIn
	 * @param nightOut
	 * @param workDesc
	 * @param emp_id
	 * @return
	 * 		<p>
	 *         Success Response : { response: { data: [1] 0: { workDate:
	 *         "10-10-2015" lunchOut: null nightOut: "23:00" in: "9:00" lunchIn:
	 *         null nightIn: "21:00" workDesc: "my new entry" out: "19:00" }- -
	 *         success: true rcode: null error: null }- }
	 *         <p>
	 *         When user enter or update time sheet value successfully.
	 * 
	 *         <p>
	 *         Fail Response : Fail Response :{ response: { data: [1] 0: { msg:
	 *         null }- - success: false rcode: "505" error: "invalid user" }- }
	 *         <p>
	 *         In case of invalid use or empId
	 */
	@Path("/timeEntry")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject timeEntry(@FormParam("lunchIn") String lunchIn, @FormParam("in") String in,
			@FormParam("out") String out, @FormParam("workDate") String workdate,
			@FormParam("lunchOut") String lunchOut, @FormParam("nightIn") String nightIn,
			@FormParam("nightOut") String nightOut, @FormParam("workDesc") String workDesc,
			@FormParam("empId") String emp_id) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			if (workdate != null && !workdate.equalsIgnoreCase("")) {
				if (emp_id != null && !emp_id.equalsIgnoreCase("")) {
					if (util.dateValidation(sdf.parse(workdate), "E")) {
						int id = Integer.parseInt(emp_id);
						timeSchedual.timeSheetValue(lunchIn, in, out, workdate, lunchOut, nightIn, nightOut, id);
						reqParser.setDateSheetValue(workDesc, workdate, id);
						timeSchedual.dateSheetValue();
						timeSchedual.clearMap();
					} else {
						jsonResService.errorResponse("Record is not avail.");
					}
				} else {
					jsonResService.errorResponse("emp_id can not be null");
				}
			} else {
				jsonResService.errorResponse("Date can not be null");
			}
		} catch (Exception ex) {
			jsonResService.errorResponse(new TrackerException("Invalid user entry").getMessage());
		}
		return jsonResService.responseSender();
	}

	/**
	 * @Required url to redirect :
	 *           http://time-tracker-backend-app.herokuapp.com/Tracker/rest/
	 *           employee /forgotPwd
	 * @POST
	 * 		<p>
	 *       Description : To reset password for existing user. We send email to
	 *       registered mail id to set the new password.
	 * @param email
	 * @return
	 * 		<p>
	 *         Success Response : { response: { data: [1] 0: { firstName: "abc"
	 *         lastName: "xyz" address: "indore" gender: "m" dob: 580780800000
	 *         contact: "0123456789" id: 2 email: "abc@newput.com" doj:
	 *         1442188800000 }- - success: true rcode: null error: null }- }
	 *         <p>
	 *         When user enter the registered mail id it send the mail to the
	 *         specified email id.
	 * 
	 *         <p>
	 *         Fail Response : Fail Response :{ response: { data: [1] 0: { msg:
	 *         null }- - success: false rcode: "505" error: "invalid user" }- }
	 *         <p>
	 *         In case of invalid use or mail id.
	 * 
	 */
	@Path("/forgotPwd")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject forgotPwd(@FormParam("email") String email) {
		try {
			if (email != null && !email.equalsIgnoreCase("") && util.mailFormat(email)) {
				String ptoken = util.generateRandomString();
				empService.resetPassword(email, ptoken, "password");
				if (jsonResService.isSuccess()) {
					emailSend.sendMail("password");
				}
			} else {
				jsonResService.errorResponse("Mail id can not be null and in proper format");
			}
		} catch (Exception ex) {
			jsonResService.errorResponse(new TrackerException("Internal Server Error").getMessage());
		}
		return jsonResService.responseSender();
	}

	/**
	 * @Required url to redirect :
	 *           http://time-tracker-backend-app.herokuapp.com/Tracker/rest/
	 *           employee/excelExport
	 * @POST
	 * 		<p>
	 *       Description : To download the monthly excel sheet from UI.
	 * @param emp_id
	 * @param monthName
	 * @param year
	 * @return
	 * 		<p>
	 *         Success Response : Pop generated to download the excel sheet.
	 *         <p>
	 *         Fail Response : No Response
	 */
	@Path("/excelExport")
	@POST
	@Produces("application/vnd.ms-excel")
	public Response excelExport(@FormParam("empId") String emp_id, @FormParam("month") String monthName,
			@FormParam("year") String year) {
		ResponseBuilder response = null;
		File file = null;
		if (emp_id != null && !emp_id.equalsIgnoreCase("")) {
			if (monthName != null && !monthName.equalsIgnoreCase("")) {
				if (util.validCheck(monthName, year)) {
					file = excelTimeSheet.createExcelSheet(Integer.parseInt(emp_id), monthName, year);
					String[] parts = file.getPath().split("tempfile");
					String part1 = parts[0];
					File newFile = new File(part1 + "time_sheet.xls");
					if (newFile.exists()) {
						newFile.delete();
						newFile = new File(part1 + "time_sheet.xls");
					}
					file.renameTo(newFile);
					response = Response.ok((Object) newFile);
					response.header("Content-Disposition", "attachment; filename=time-sheet.xls");
					if (file.exists()) {
						file.delete();
					}
				}
			}
		}
		return response.build();
	}

	/**
	 * @Required url to redirect :
	 *           http://time-tracker-backend-app.herokuapp.com/Tracker/rest/
	 *           employee/pwdVerify
	 * @POST
	 * 		<p>
	 *       Description : To set the new password through redirecting the url
	 *       send to the email.
	 * @param empId
	 * @param pToken
	 * @param newPwd
	 * @return
	 * 		<p>
	 *         Success Response : { response: { data: [1] 0: { firstName: "abc"
	 *         lastName: "xyz" address: "indore" gender: "m" dob: 580780800000
	 *         contact: "0123456789" id: 2 email: "abc@newput.com" doj:
	 *         1442188800000 }- - success: true rcode: null error: null }- }
	 *         <p>
	 *         When user enter the registered mail id it send the mail to the
	 *         specified email id.
	 * 
	 *         <p>
	 *         Fail Response : Fail Response :{ response: { data: [1] 0: { msg:
	 *         null }- - success: false rcode: "505" error: "invalid user" }- }
	 *         <p>
	 *         In case of invalid use or mail id or token.
	 */
	@Path("/pwdVerify")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject passwordVerification(@FormParam("empId") int empId, @FormParam("pToken") String pToken,
			@FormParam("newPassword") String newPwd) {
		try {
			if (empId > 0) {
				if (pToken != null && !pToken.equalsIgnoreCase("")) {
					if (newPwd != null && !newPwd.equalsIgnoreCase("")) {
						newPwd = util.md5(newPwd);
						reqParser.setPValidationValue(empId, pToken, newPwd);
						empService.pwdVerify(emp);
					} else {
						jsonResService.errorResponse("Password can not be blank");
					}
				} else {
					jsonResService.errorResponse("token can not be blank");
				}
			} else {
				jsonResService.errorResponse("Mail id can not be null");
			}
		} catch (Exception ex) {
			jsonResService.errorResponse(new TrackerException("Invalid user").getMessage());
		}
		return jsonResService.responseSender();
	}

	/**
	 * @Required url to redirect :
	 *           http://time-tracker-backend-app.herokuapp.com/Tracker/rest/
	 *           employee/monthlyExcel
	 * @POST
	 * 		<p>
	 *       Description : To provide Json object of time sheet to UI respose.
	 * @param monthName
	 * @param emp_id
	 * @param year
	 * @return
	 * 		<p>
	 *         Success Response : { response: { data: [3] 0: { workDate:
	 *         "01-10-2015" lunchOut: "07:00" nightOut: "16:10" in: "03:30"
	 *         totalHour: "10:15" lunchIn: "06:25" nightIn: "15:15" workDesc:
	 *         "this is my 2 day" out: "13:25" }- 1: { workDate: "03-10-2015"
	 *         lunchOut: "06:30" nightOut: "16:15" in: "03:30" totalHour:
	 *         "10:00" lunchIn: "06:00" nightIn: "15:15" workDesc:
	 *         "this is my 4 day" out: "13:00" }- 2: { workDate: "05-10-2015"
	 *         lunchOut: "07:00" nightOut: "16:10" in: "03:30" totalHour:
	 *         "10:15" lunchIn: "06:25" nightIn: "15:15" workDesc:
	 *         "this is my 6 day" out: "13:25" }- 3: { workDate: "07-10-2015"
	 *         lunchOut: "06:15" nightOut: "16:10" in: "03:30" totalHour:
	 *         "10:20" lunchIn: "05:50" nightIn: "15:10" workDesc:
	 *         "this is my 8 day" out: "13:15" }- }- }
	 *         <p>
	 *         When user enter the registered mail id it send the mail to the
	 *         specified email id.
	 * 
	 *         <p>
	 *         Fail Response : Fail Response :{ response: { data: [1] 0: { msg:
	 *         null }- - success: false rcode: "505" error: "invalid user" }- }
	 *         <p>
	 *         In case of invalid use or mail id or token.
	 */
	@Path("/monthlyExcel")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject monthlyExcel(@FormParam("month") String monthName, @FormParam("empId") String emp_id,
			@FormParam("year") String year) {
		try {
			if (emp_id != null && !emp_id.equalsIgnoreCase("")) {
				if (monthName != null && !monthName.equalsIgnoreCase("")) {
					if (util.validCheck(monthName, year)) {
						excel.monthSheet(monthName, Integer.parseInt(emp_id), year);
					} else {
						jsonResService.errorResponse("Record is not avail.");
					}
				} else {
					jsonResService.errorResponse("Please provide the month to select data");
				}
			} else {
				jsonResService.errorResponse("Please provide employee id to select data");
			}
		} catch (Exception ex) {
			jsonResService.errorResponse(new TrackerException("Invalid user").getMessage());
		}
		return jsonResService.responseSender();
	}

	/**
	 * @Required url to redirect :
	 *           http://time-tracker-backend-app.herokuapp.com/Tracker/rest/
	 *           employee/mailExcelSheet
	 * @POST
	 * 		<p>
	 *       Description : To mail excel sheet on the registered mail id.
	 * @param emp_id
	 * @param monthName
	 * @param year
	 * @return
	 * 		<p>
	 *         Success Response :{ response: { data: [1] 0: { msg:
	 *         "Your time sheet succefully send to your registered mail id." }-
	 *         - success: true rcode: null error: null }- }
	 * 
	 *         <p>
	 *         Fail Response : Fail Response :{ response: { data: [1] 0: { msg:
	 *         null }- - success: false rcode: "505" error: "invalid user" }- }
	 *         <p>
	 *         In case of invalid use or mail id or token.
	 */
	@Path("/mailExcelSheet")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject mailExcelSheet(@FormParam("empId") String emp_id, @FormParam("month") String monthName,
			@FormParam("year") String year) {
		try {
			if (emp_id != null && !emp_id.equalsIgnoreCase("")) {
				if (monthName != null && !monthName.equalsIgnoreCase("")) {
					if (util.validCheck(monthName, year)) {
						File file = excelTimeSheet.createExcelSheet(Integer.parseInt(emp_id), monthName, year);
						if (jsonResService.isSuccess()) {
							emailSend.sendExcelSheet(excelTimeSheet.getEmpEmail(Integer.parseInt(emp_id)), file);
							file.delete();
						}
					} else {
						jsonResService.errorResponse("Record is not avail.");
					}
				} else {
					jsonResService.errorResponse("Please provide the month to select data");
				}
			} else {
				jsonResService.errorResponse("Please provide employee id to select data");
			}
		} catch (Exception ex) {
			jsonResService.errorResponse(new TrackerException("Invalid user").getMessage());
		}
		return jsonResService.responseSender();
	}

	/**
	 * @Required url to redirect :
	 *           http://time-tracker-backend-app.herokuapp.com/Tracker/rest/
	 *           employee/resend
	 * @POST
	 * 		<p>
	 *       Description : To resent the verification mail on the registered
	 *       mail id of user for registration and password.
	 * @param email
	 * @param flag
	 * @return
	 * 		<p>
	 *         Success Response :{ response: { data: [1] 0: { msg:
	 *         "Your time sheet succefully send to your registered mail id." }-
	 *         - success: true rcode: null error: null }- }
	 * 
	 *         Fail Response ; { response: { data: [1] 0: { msg: null }- -
	 *         success: false rcode: "505" error: "Internal Server Error " }- }
	 */
	@Path("/resend")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject resendMail(@FormParam("email") String email, @FormParam("flag") String flag) {
		try {
			if (email != null && !email.equalsIgnoreCase("") && util.mailFormat(email)) {
				if (flag != null && !flag.equalsIgnoreCase("")) {
					String token = util.generateRandomString();
					empService.resetPassword(email, token, flag);
					if (jsonResService.isSuccess()) {
						emailSend.sendMail(flag);
					}
				} else {
					jsonResService.errorResponse("Flag is must");
				}
			} else {
				jsonResService.errorResponse("Email id not valid");
			}
		} catch (Exception ex) {
			jsonResService.errorResponse(new TrackerException("Internal Server Error ").getMessage());
		}
		return jsonResService.responseSender();
	}

	/**
	 * @Required url to redirect :
	 *           http://time-tracker-backend-app.herokuapp.com/Tracker/rest/
	 *           employee/signOut
	 * @POST
	 * 		<p>
	 *       Description : To logout from the API.
	 * @param emp_id
	 * @return
	 */
	@Path("/signOut")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject signOut(@FormParam("empId") String emp_id) {
		try {
			loginService.signOut(Integer.parseInt(emp_id));
		} catch (Exception ex) {
			jsonResService.errorResponse(new TrackerException("Invalid user").getMessage());
		}
		return jsonResService.responseSender();
	}
}
