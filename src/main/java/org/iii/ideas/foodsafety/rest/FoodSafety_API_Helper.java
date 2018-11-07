package org.iii.ideas.foodsafety.rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FoodSafety_API_Helper {// 字串陣列建議用List<String>處理，可彈性調整大小
	private static final Logger logger = LogManager.getLogger(AFS_ini.class);

	public void checkSurveyDate(String foodList, String supList,String manufacture,String kitchen, String suvDate, String data_id, String reportDate)
			throws Exception {
		ES_helper es = new ES_helper();
		logger.info("checkSurveyDate");
		String std = "";
		String spd = "";
		new Common_Function();
		List<String> b1mf1m = Common_Function
				.readFile(getClass().getClassLoader().getResource(AFS_ini.dailyQuery_dictionaryList[0]).getPath());
		new Common_Function();
		List<String> b3mf1m = Common_Function
				.readFile(getClass().getClassLoader().getResource(AFS_ini.dailyQuery_dictionaryList[1]).getPath());
		new Common_Function();
		List<String> b7df7d = Common_Function
				.readFile(getClass().getClassLoader().getResource(AFS_ini.dailyQuery_dictionaryList[2]).getPath());
		List<String> tmp_food = new ArrayList<String>();
		List<String> tmp_sup = new ArrayList<String>();
		if (foodList.contains(",")) {
			tmp_food = Arrays.asList(foodList.split(","));
		} else {
			tmp_food.add(foodList);
		}
		if (supList.contains(",")) {// 可能需要判斷null問題
			tmp_sup = Arrays.asList(supList.split(","));
		} else {
			tmp_sup.add(supList);
		}

		for (int k = 0; k < tmp_food.size(); k++) {

			DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			Date date = format.parse(suvDate);
			Calendar c = Calendar.getInstance();
			if (b1mf1m.contains(tmp_food.get(k))) {
				c.setTime(date);
				c.add(Calendar.MONTH, -1);
				std = format.format(c.getTime());
				c.setTime(date);
				c.add(Calendar.MONTH, +1);
				spd = format.format(c.getTime());
			} else if (b3mf1m.contains(tmp_food.get(k))) {
				c.setTime(date);
				c.add(Calendar.MONTH, -3);
				std = format.format(c.getTime());
				c.setTime(date);
				c.add(Calendar.MONTH, +1);
				spd = format.format(c.getTime());
			} else if (b7df7d.contains(tmp_food.get(k))) {
				c.setTime(date);
				c.add(Calendar.DATE, -7);
				std = format.format(c.getTime());
				c.setTime(date);
				c.add(Calendar.DATE, +7);
				spd = format.format(c.getTime());
			} else {
				c.setTime(date);
				c.add(Calendar.MONTH, -3);
				std = format.format(c.getTime());
				c.setTime(date);
				c.add(Calendar.MONTH, +1);
				spd = format.format(c.getTime());
			}

			for (int j = 0; j < tmp_sup.size(); j++) {
				if (tmp_sup.get(j) == null) {
					continue;
				}
				System.out.println("===================");
				System.out.println(tmp_sup.get(j));
				System.out.println(tmp_food.get(k));
				System.out.println(std);
				System.out.println(spd);
				System.out.println("===================");
				sendPost(tmp_sup.get(j),kitchen,manufacture, tmp_food.get(k), std, spd, es.es_GetSiteNameById(data_id),
						es.es_GetSourceTitleById(data_id), es.es_GetUrlById(data_id), reportDate);
			}
		}

	}

	public void sendPost(String supplier, String kitchen, String manufacture, String food, String st_date_fs,
			String sp_date_fs, String dataType_fs, String dataSubject, String dataUrl_fs, String reportDate)
			throws Exception {
		logger.info("Sending request to CateringService platform");
		HttpsURLConnection conn = null;
		//URL url = new URL("https://172.16.3.8/cateringservice/rest/API/");
		URL url = new URL("https://61.64.53.225/cateringservice/rest/API/");
		//URL url = new URL("https://175.98.115.161/cateringservice/rest/API/");
		conn = (HttpsURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		// SSL setting
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, new TrustManager[] { new javax.net.ssl.X509TrustManager() {

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
		} }, null);
		conn.setSSLSocketFactory(context.getSocketFactory());
		String data = "{\"method\":\"QueryFoodSafe\",\"args\":{\"verifyCode\":\"f1x6VjVZ\"," + "\"menuStartDate\":\""
				+ st_date_fs + "\"," + "\"menuEndDate\":\"" + sp_date_fs + "\"," + "\"supplier\":\"" + supplier
				+ "\"," + "\"manufacturer\":\"" + manufacture + "\"," + "\"offerKitchen\":\""
				+ kitchen + "\"," + "\"ingredientName\":\"" + food + "\"," + "\"source\":\"" + dataType_fs + "\","
				+ "\"sourceSubject\":\"" + dataSubject + "\"," + "\"reportDate\":\"" + reportDate + "\","
				+ "\"sourceUrl\":\"" + dataUrl_fs + "\"," + "\"email\": \"moeiii.2557+qqq@gmail.com\"}}";
		System.out.println(data);
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.write(data.getBytes("utf-8"));// 為了傳送utf8的文字改成這個
		wr.flush();
		wr.close();
		int responseCode = conn.getResponseCode();
		logger.info("Response Code(QueryFoodSafe) : " + responseCode);
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		data = "{\"method\":\"QueryFoodSafeStreet\",\"args\":{\"verifyCode\":\"f1x6VjVZ\"," + "\"useStartDate\":\""
				+ st_date_fs + "\"," 
				+ "\"useEndDate\":\"" + st_date_fs + "\"," 
				+ "\"supplier\":\"" + supplier + "\","
				+ "\"manufacturer\":\"" + manufacture + "\"," 
				+ "\"offerKitchen\":\"" + kitchen+ "\"," 
				+ "\"ingredientName\":\"" + food + "\"," 
				+ "\"source\":\"" + dataType_fs + "\","
				+ "\"sourceSubject\":\"" + dataSubject + "\"," 
				+ "\"reportDate\":\"" + reportDate + "\"," 
				+ "\"sourceUrl \":\"" + dataUrl_fs + "\"," 
				+ "\"email\":\"moeiii.2557+qqq@gmail.com\"}}";
		wr.write(data.getBytes("utf-8"));// 為了傳送utf8的文字改成這個
		wr.flush();
		wr.close();
		logger.info("Response Code(QueryFoodSafeStreet) : " + responseCode);
	}
}
