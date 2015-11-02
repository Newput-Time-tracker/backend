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

		if(SystemConfig.get("CRON_SERVICE").equalsIgnoreCase("start")){
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
	 * Description : Schedule the Cron jobs for mail sending.
	 */
	public void emailSendJob() {

		if(SystemConfig.get("CRON_SERVICE").equalsIgnoreCase("start")){
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
						file.delete();
					}
				}
			}	
		}
	}
}
