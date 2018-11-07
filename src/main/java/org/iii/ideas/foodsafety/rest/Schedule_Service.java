package org.iii.ideas.foodsafety.rest;

import java.io.File;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import static org.elasticsearch.common.xcontent.XContentFactory.*;

@Service
public class Schedule_Service {
	private static final Logger logger = LogManager.getLogger(AFS_ini.class);

	@SuppressWarnings("static-access")
	@Scheduled(cron = "0 */5 * ? * MON-FRI")
	// @Scheduled(fixedDelay = 10000)
	public void dailyFoodSaftyFromGmail() throws UnknownHostException {
		ES_helper esh = new ES_helper();
		Client client = esh.es_con();
		logger.info("dailyFoodSaftyFromGmail");
		try {

			GmailClient newGmailClient = new GmailClient();
			newGmailClient.setAccountDetails(AFS_ini.gmailclient_Account, AFS_ini.gmailclient_Password);
			List<File> fileList = newGmailClient.readGmail();
			System.out.println("==================:" + fileList.size());

			for (int a = 0; a < fileList.size(); a++) {
				String[][] result = new ExcelHandler().getData(fileList.get(a).getAbsolutePath());
				for (int i = 0; i < result.length; i++) {
					System.out.println(result.length);
					System.out.println(i);
					// client.prepareSearch

					String Rtime = String.valueOf(Integer.valueOf(result[i][0].substring(0, 3)) + 1911) + "_"
							+ result[i][0].substring(3, 5);
					String hs = result[i][0].substring(0, 7) + "-" + result[i][3] + "-" + result[i][2];

					System.out.println("======================:" +getMD5( hs));
					SearchResponse resp = client.prepareSearch(AFS_ini.searchedES_indexName).setTypes("gmail_lai")
							.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
							.setQuery(QueryBuilders.termQuery("hashcode", getMD5(hs))).setRouting(Rtime)
							.setExplain(false).get();
					if (resp.getHits().getTotalHits() == 0) {
						System.out.println("@@@@@@@@@@@@@");

						System.out.println(resp.getHits().getTotalHits());
						System.out.println(Rtime);
						XContentBuilder builder = jsonBuilder().startObject()
								.field("content",
										result[i][0].substring(0, 7) + "_" + result[i][3] + "_" + result[i][2])
								.field("hashcode", getMD5(hs)).field("tags_content", result[i][3])
								.field("tags", "-1").field("title", "gmail_lai").field("sitename", "行政院農業委員會農糧署")
								.field("date", result[i][0].substring(0, 7)).field("author", "gmail_lai").endObject();
						client.prepareIndex(AFS_ini.searchedES_indexName, "gmail_lai").setSource(builder)
								.setRouting(Rtime).get();
					}

				}

			}

			fileList.clear();
			logger.info("dailyFoodSaftyFromGmail done");
			// return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			// return false;
		} finally {
			esh.es_close(client);
		}

	}

	public static String getMD5(String str) throws NoSuchAlgorithmException {

		// 生成一个MD5加密计算摘要
		MessageDigest md = MessageDigest.getInstance("MD5");
		// 计算md5函数
		md.update(str.getBytes());
		// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
		// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
		return new BigInteger(1, md.digest()).toString(16);

	}

}
