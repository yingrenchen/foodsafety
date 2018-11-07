package org.iii.ideas.foodsafety.rest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Common_Function {
	private static final Logger logger = LogManager.getLogger(AFS_ini.class);

	public static HashSet<String> readDic(String DicfilePath) throws IOException {
		logger.info("readDic_read file:" + DicfilePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(DicfilePath), "UTF-8"));
		String line, tempstring;
		String[] tempArray = null;
		ArrayList<String> myList = new ArrayList<String>();
		int i = 0;
		while ((line = br.readLine()) != null) {
			tempstring = line.replace(" ", "").replace("　", "").replace("（", "(").replace("）", ")").replace("(", "\\(")
					.replace(")", "\\)").replace(":", "\\:");
			tempArray = tempstring.split("\\s");
			for (i = 0; i < tempArray.length; i++) {
				myList.add(tempArray[i]);
			}
		}
		HashSet<String> dic_key = new HashSet<String>(myList);
		br.close();
		return dic_key;
	}

	public static List<String> readFile(String FileName) throws IOException {
		FileReader fr = new FileReader(FileName);
		List<String> readString = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FileName), "UTF-8"));
		String line;
		while ((line = br.readLine()) != null) {
			readString.add(line);
		}
		br.close();
		fr.close();
		return readString;

	}

	public static String getDateTime() {
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		String strDate = sdFormat.format(date);
		return strDate;
	}

}
