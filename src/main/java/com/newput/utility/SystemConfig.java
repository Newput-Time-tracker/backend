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
		
		setDataBaseInfo(get("CLEARDB_DATABASE_URL"));
	}

	public static String get(String propName) {
		return props.getProperty(propName);
	}
	
	public void setDataBaseInfo(String dbUrl){
		//String dbUrl = "mysql://b556cf1796327c:6c851d00@us-cdbr-iron-east-03.cleardb.net/heroku_a540dae71714038?reconnect=true";

		String s[] = dbUrl.split(":");
		String user = s[1].substring(2);
		props.setProperty("DB_USER", user);
		
		int i1 = s[2].indexOf("@");
		String password = s[2].substring(0, i1);
		props.setProperty("DB_PASSWORD", password);		
		
		int i2 = s[2].indexOf("/");
		String localHost = s[2].substring(i1+1, i2);
		props.setProperty("DB_LOCAL_HOST", localHost);		
		
		int i3 = s[2].indexOf("?");
		String dbName = s[2].substring(i2, i3);
		props.setProperty("DB_NAME", dbName);
	}
}
