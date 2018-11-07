package org.iii.ideas.foodsafety.rest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AFS_ini {

	public static int dailyQuery_DelayTimeInterval = 5;//minutes
	public static String[] dailyQuery_dictionaryList = { "b1mf1m.txt", "b3mf1m.txt", "b7df7d.txt" };
	public static String gmailFolder_Path = "/home/yrchen/edata";
	public static String gmailclient_Account = "moeiii.2557";
	public static String gmailclient_Password = "iii66072557";
	public static String searchedES_indexName = "food_safety";
	public static String[] searchedES_dictionaryList = { "supplier.txt", "food.txt" };
	public static String elasticsearch_ip="172.16.7.3";
	//public static String elasticsearch_ip="175.98.115.40";//測試用外部連結IP
	public static int elasticsearch_port=9300;
	public static String elasticsearch_clusterName="FS-Cluster";
	private static final Logger logger = LogManager.getLogger(AFS_ini.class);
	
	public void AFS_init() throws IOException {
		Properties prop = new Properties();
		String propFileName = "AFS_ini.properties";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		if (inputStream != null) {
			prop.load(inputStream);
			logger.info("property file '" + propFileName + "' load");
		} else {
			logger.error("property file '" + propFileName + "' not found in the classpath");
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		
		}

	}

}
