package com.newput.utility;

import java.util.Properties;

public class SystemConfig {

	private static Properties props;

	public SystemConfig() {
		props = new Properties();
		//props.setProperty("WEBAPP_URL", System.getenv("WEBAPP_URL"));
		props.setProperty("MAIL_USERID", System.getenv("MAIL_USERID"));
		props.setProperty("MAIL_PASSWORD", System.getenv("MAIL_PASSWORD"));
	}

	public static String get(String propName) {
		return props.getProperty(propName);
	}
}
