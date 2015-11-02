package com.newput.utility;

import java.util.Properties;

public class SystemConfig {

	private static Properties props;

	public SystemConfig() {
		props = new Properties();
		props.setProperty("CRON_SERVICE", System.getenv("CRON_SERVICE"));
		props.setProperty("CLEARDB_DATABASE_URL", System.getenv("CLEARDB_DATABASE_URL"));
		props.setProperty("WEBAPP_URL", System.getenv("WEBAPP_URL"));
		props.setProperty("MAIL_USERID", System.getenv("MAIL_USERID"));
		props.setProperty("MAIL_PASSWORD", System.getenv("MAIL_PASSWORD"));
		
		props.setProperty("MAIL_PROTOCOL", System.getenv("MAIL_PROTOCOL"));
		props.setProperty("MAIL_AUTH", System.getenv("MAIL_AUTH"));
		props.setProperty("MAIL_ENABLE", System.getenv("MAIL_ENABLE"));
		props.setProperty("MAIL_HOST", System.getenv("MAIL_HOST"));
		props.setProperty("MAIL_PORT", System.getenv("MAIL_PORT"));
	}

	public static String get(String propName) {
		return props.getProperty(propName);
	}
}
