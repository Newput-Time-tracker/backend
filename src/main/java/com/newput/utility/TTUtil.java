package com.newput.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import java.util.Random;

import org.apache.commons.codec.binary.Hex;
import org.apache.openjpa.lib.util.Base16Encoder;
import org.springframework.stereotype.Service;

/**
 * * @author Newput
 *
 */
@Service
public class TTUtil {

	private static final String CHAR_LIST = "1234567890";
	private static final int RANDOM_STRING_LENGTH = 4;

	/**
	 * Description : To create session token for the user.
	 * 
	 * @param id
	 * @param email
	 * @return
	 */
	public String createSessionKey(Long id, String email) {
		try {
			return Base16Encoder.encode(MessageDigest.getInstance("MD5")
					.digest((email + "-" + System.currentTimeMillis() + id).getBytes()));
		} catch (NoSuchAlgorithmException e) {
			return e.getMessage();
		}
	}

	/**
	 * Description : Use to encrypt password of a user.
	 * 
	 * @param str
	 * @return
	 */
	public String md5(String str) {
		if (str != null && !str.equalsIgnoreCase("")) {
			MessageDigest messageDigest = null;
			try {
				messageDigest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			messageDigest.reset();
			messageDigest.update(str.getBytes());
			return new String(Hex.encodeHex(messageDigest.digest()));
		} else {
			return "";
		}
	}

	public String getAlphaNum(String s) {
		if (s == null) {
			return "-";
		} else {
			return s.replaceAll("[^A-Za-z]+", "");
		}
	}

	/**
	 * Description : To check the mail format.
	 * 
	 * @param email
	 * @return
	 */
	public boolean mailFormat(String email) {
		Boolean valid = false;
		String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		valid = email.matches(EMAIL_REGEX);
		return valid;
	}

	/**
	 * Description : To check the valid contact number.
	 * 
	 * @param contact
	 * @return
	 */
	public String getIntNum(String contact) {
		if (contact.startsWith("+"))
			contact = contact.substring(1);
		if (contact.matches("\\d{12}") && contact.startsWith("91")) {
			contact = contact.substring(2);
		}
		if (contact.matches("\\d{11}") && contact.startsWith("0")) {
			contact = contact.substring(1);
		}
		if (contact.matches("\\d{10}")) {
			return contact;
		} else {
			return "";
		}
	}

	/**
	 * Description : Pasrse epoch time to HH:MM format.
	 * 
	 * @param timeValue
	 * @return e.g. "09:30"
	 */
	public String timeHrs(Long timeValue, Long workDate) {
		try {
			Long nextDate = workDate + 86400000;
			Calendar calendar = Calendar.getInstance();
			Date today = new Date(timeValue);
			calendar.setTime(today);
			String hours = "" + calendar.get(Calendar.HOUR_OF_DAY);
			if (hours.length() == 1) {
				hours = "0" + hours;
			}
			String minute = "" + calendar.get(Calendar.MINUTE);
			if (minute.length() == 1) {
				minute = "0" + minute;
			}
			String timeSlot = hours + ":" + minute;
			if (!(timeValue.equals(nextDate)) && (timeSlot.equals("0:0") || timeSlot.equals("0:00")
					|| timeSlot.equals("00:0") || timeSlot.equals("00:00"))) {
				return "";
			} else if (timeValue >= nextDate) {
				return timeSlot = (Integer.parseInt(hours) + 24) + ":" + minute;
			} else {
				return timeSlot;
			}
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Description : use to create a AlphaNumeric token for the verification of
	 * email
	 * 
	 * @return it return a random generated string e.g.k3gctKho,7lKfGnIV
	 */
	public String generateRandomString() {
		StringBuffer randStr = new StringBuffer();
		for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
			int number = getRandomNumber();
			char ch = CHAR_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}

	/**
	 * Description : method call internally to add numeric value in verification
	 * token
	 * 
	 * @return
	 */
	private int getRandomNumber() {
		int randomInt = 0;
		Random randomGenerator = new Random();
		randomInt = randomGenerator.nextInt(CHAR_LIST.length());
		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}

	/**
	 * Description : To get the min and max date of the requested month.
	 * 
	 * @param monthName
	 * @param year
	 * @return
	 */
	public HashMap<String, Long> getMonthlyDate(String monthName, String year) {
		HashMap<String, Long> map = new HashMap<String, Long>();
		int reqYear = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();

			if (year != null && !year.equalsIgnoreCase("")) {
				reqYear = Integer.parseInt(year);
			} else {
				reqYear = cal.get(Calendar.YEAR);
			}

			int currMnth = cal.get(Calendar.MONTH) + 1;
			cal.setTime(new SimpleDateFormat("MMM").parse(monthName));
			int reqMnth = cal.get(Calendar.MONTH) + 1;

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, reqYear);
			calendar.add(Calendar.MONTH, -(currMnth - reqMnth));
			int min = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, min);
			long minDate = sdf.parse(sdf.format(calendar.getTime())).getTime();
			map.put("minDate", minDate);

			int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, max);
			long maxDate = sdf.parse(sdf.format(calendar.getTime())).getTime();
			map.put("maxDate", maxDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * Description : Use to convert the HH:MM format into time epoch.
	 * 
	 * @param workDate
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public Long timeMiliSec(String workDate, String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = sdf.parse(workDate);
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		String delims = ":";
		String[] tokens = time.split(delims);
		int hrs = Integer.parseInt(tokens[0]);
		int min = Integer.parseInt(tokens[1]);
		calender.set(Calendar.HOUR, hrs);
		calender.set(Calendar.MINUTE, min);
		return calender.getTimeInMillis();
	}

	/**
	 * Description : To parse date according to the excel sheet format.
	 * 
	 * @param workDate
	 * @return
	 */
	public int getExcelSheetDate(Long workDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String dateString = formatter.format(workDate);
		Calendar cal = Calendar.getInstance();
		try {
			Date date = formatter.parse(dateString);
			cal.setTime(date);
			return cal.get(Calendar.DAY_OF_MONTH);
		} catch (ParseException e) {
			e.printStackTrace();
			return 38;
		}
	}

	/**
	 * Description : Validate the date of dob and doj.
	 * 
	 * @param date
	 * @param flag
	 * @return
	 */
	public boolean dateValidation(Date date, String flag) {
		try {
			int year;
			Long minDate = 0L;
			if (flag.equals("E")) {
				minDate = 1443637800000L;
			} else {
				minDate = -757402201000L;
			}
			Long userDate = date.getTime();

			Calendar now = Calendar.getInstance();
			if (flag.equals("dob")) {
				now.set(Calendar.YEAR, now.get(Calendar.YEAR) - 17);
				year = now.get(Calendar.YEAR);
			} else {
				year = now.get(Calendar.YEAR);
			}
			int mnth = now.get(Calendar.MONTH);
			int currDate = now.get(Calendar.DATE);
			now.set(year, mnth, currDate);
			long currValue = now.getTimeInMillis();

			if (userDate >= minDate && userDate <= currValue) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Description : Validation on the date insertion for the time sheet.
	 * 
	 * @param month
	 * @param year
	 * @return
	 */
	public boolean validCheck(String month, String year) {
		Long startDate = 1443637800000L; // 01-10-2015
		int reqYear = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new SimpleDateFormat("MMM").parse(month));
			int reqMnth = calendar.get(Calendar.MONTH);
			calendar = Calendar.getInstance();
			if (year != null && !year.equalsIgnoreCase("")) {
				reqYear = Integer.parseInt(year);
			} else {
				reqYear = calendar.get(Calendar.YEAR);
			}
			calendar.set(Calendar.YEAR, reqYear);
			calendar.set(Calendar.MONTH, reqMnth);
			int min = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, min);
			long minDate = sdf.parse(sdf.format(calendar.getTime())).getTime();

			calendar = Calendar.getInstance();
			Long currTime = calendar.getTimeInMillis();
			if ((minDate >= startDate) && (minDate <= currTime)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkValidWeek(String workDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		// Long newDate = sdf.parse(workDate).getTime();
		// Calendar cal = Calendar.getInstance();
		// cal.setTimeInMillis(newDate);
		// int x = cal.get(Calendar.WEEK_OF_YEAR);
		// Calendar cal1 = Calendar.getInstance();
		// int y = cal1.get(Calendar.WEEK_OF_YEAR);
		// if (x == y) {
		// return true;
		// } else {
		// return false;
		// }
		//
        int days = Integer.parseInt(SystemConfig.get("EDITING_DAYS"));
		Date newDate = (Date) sdf.parse(workDate);
		Date dates = new Date();
		long DAY_IN_MS = 1000 * 60 * 60 * 24;
		Date beforedate = new Date(dates.getTime() - (days * DAY_IN_MS));
		if (newDate.after(beforedate) && (newDate.before(dates) || newDate.equals(dates))) {
			return true;
		} else {
			return false;
		}
	}

}
