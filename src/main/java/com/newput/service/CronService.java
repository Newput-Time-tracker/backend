package com.newput.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.newput.domain.Employee;
import com.newput.domain.EmployeeExample;
import com.newput.mapper.EmployeeMapper;
import com.newput.utility.EMailSender;
import com.newput.utility.ExcelTimeSheet;
import com.newput.utility.JsonResService;
import com.newput.utility.SystemConfig;

/**
 * Description : Use to schedule the Cron jobs.
 * 
 * @author Newput
 *
 */
public class CronService {

	@Autowired
	private EmployeeMapper empMapper;

	@Autowired
	private Employee emp;

	@Autowired
	private EMailSender sendEmail;

	@Autowired
	private ExcelTimeSheet excelTimeSheet;

	@Autowired
	private JsonResService jsonResService;

	@Autowired
	private EMailSender emailSend;

	/**
	 * Description : Schedule the Cron jobs for daily notification.
	 */
	public void dailyNotification() {

		if (Boolean.parseBoolean(SystemConfig.get("DAILY_CRON_SERVICE"))) {
			List<Employee> list = new ArrayList<Employee>();
			EmployeeExample empExample = new EmployeeExample();
			empExample.createCriteria().andStatusEqualTo(true);
			list = empMapper.selectByExample(empExample);

			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					emp = list.get(i);
					sendEmail.notificationMail(emp);
				}
			}
		}
	}

	/**
	 * Description : Schedule the Cron jobs for weekly mail sending.
	 */
	public void weeklyEmailSendJob() {

//		if (Boolean.parseBoolean(SystemConfig.get("WEEKLY_CRON_SERVICE"))) {
		if (true) {
			Calendar cal = Calendar.getInstance();
			Long currntStamp = cal.getTimeInMillis();

			String year = new SimpleDateFormat("YYYY").format(currntStamp);
			String month = new SimpleDateFormat("MMMM").format(currntStamp);
			List<Employee> list = new ArrayList<Employee>();
			EmployeeExample empExample = new EmployeeExample();
			empExample.createCriteria().andStatusEqualTo(true);
			list = empMapper.selectByExample(empExample);

			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					emp = list.get(i);
					File file = excelTimeSheet.createExcelSheet(emp.getId(), month, year);
					if (jsonResService.isSuccess()) {
						emailSend.sendExcelSheet(excelTimeSheet.getEmpEmail(emp.getId()), file,
								excelTimeSheet.getTimeSheetName(emp.getId(), month, year));						
					}
					file.delete();
				}
			}
		}
	}
	
	/**
	 * Description : Schedule the Cron jobs for monthly mail sending.
	 */
	public void monthlyEmailSendJob() {
		System.out.println("inside mail method");
		if (Boolean.parseBoolean(SystemConfig.get("MONTHLY_CRON_SERVICE"))) {
			System.out.println("inside if block");
			Calendar cal = Calendar.getInstance();
			Long currntStamp = cal.getTimeInMillis();

			String year = new SimpleDateFormat("YYYY").format(currntStamp);
			String month = new SimpleDateFormat("MMMM").format(currntStamp);
			List<Employee> list = new ArrayList<Employee>();
			EmployeeExample empExample = new EmployeeExample();
			empExample.createCriteria().andStatusEqualTo(true);
			list = empMapper.selectByExample(empExample);

			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					emp = list.get(i);
					File file = excelTimeSheet.createExcelSheet(emp.getId(), month, year);
					if (jsonResService.isSuccess()) {
						emailSend.sendExcelSheet(excelTimeSheet.getEmpEmail(emp.getId()), file,
								excelTimeSheet.getTimeSheetName(emp.getId(), month, year));						
					}
					file.delete();
				}
			}
		}
	}
}
