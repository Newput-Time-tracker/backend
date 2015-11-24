package com.newput.utility;

import java.util.Properties;

public class SystemConfig {

	private static Properties props;

	public SystemConfig() {
		props = new Properties();
		props.setProperty("DAILY_CRON_SERVICE", System.getenv("DAILY_CRON_SERVICE").trim());
		props.setProperty("DAILY_CRON_SERVICE_TIME", System.getenv("DAILY_CRON_SERVICE_TIME").trim());
		props.setProperty("WEEKLY_CRON_SERVICE", System.getenv("WEEKLY_CRON_SERVICE").trim());
		props.setProperty("WEEKLY_CRON_SERVICE_TIME", System.getenv("WEEKLY_CRON_SERVICE_TIME").trim());
		props.setProperty("MONTHLY_CRON_SERVICE", System.getenv("MONTHLY_CRON_SERVICE").trim());
		props.setProperty("MONTHLY_CRON_SERVICE_TIME", System.getenv("MONTHLY_CRON_SERVICE_TIME").trim());
		props.setProperty("EDITING_DAYS", System.getenv("EDITING_DAYS").trim());
		props.setProperty("CLEARDB_DATABASE_URL", System.getenv("CLEARDB_DATABASE_URL").trim());
		props.setProperty("WEBAPP_URL", System.getenv("WEBAPP_URL").trim());
		props.setProperty("MAIL_USERID", System.getenv("MAIL_USERID").trim());
		props.setProperty("MAIL_PASSWORD", System.getenv("MAIL_PASSWORD").trim());
		
		props.setProperty("MAIL_PROTOCOL", System.getenv("MAIL_PROTOCOL").trim());
		props.setProperty("MAIL_AUTH", System.getenv("MAIL_AUTH").trim());
		props.setProperty("MAIL_ENABLE", System.getenv("MAIL_ENABLE").trim());
		props.setProperty("MAIL_HOST", System.getenv("MAIL_HOST").trim());
		props.setProperty("MAIL_PORT", System.getenv("MAIL_PORT").trim());		
		
		setDataBaseInfo(get("CLEARDB_DATABASE_URL"));
	}

	public static String get(String propName) {
		return props.getProperty(propName);
	}
	
	public void setDataBaseInfo(String dbUrl){
		String s[] = dbUrl.split(":");
		
		props.setProperty("DB_USER", s[1].substring(2));
		props.setProperty("DB_PASSWORD", s[2].substring(0, s[2].indexOf("@")));
		props.setProperty("DB_LOCAL_HOST", s[2].substring((s[2].indexOf("@")+1), s[2].indexOf("/")));
		props.setProperty("DB_NAME", s[2].substring(s[2].indexOf("/"), s[2].indexOf("?")));
	}
}
