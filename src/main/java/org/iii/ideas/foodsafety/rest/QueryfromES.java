package org.iii.ideas.foodsafety.rest;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class QueryfromES {
	final static Map<String, Object> urlList_map = new HashMap<String, Object>();
	final static Map<String, Object> contentList_map = new HashMap<String, Object>();
	final static Map<String, Object> tagsList_map = new HashMap<String, Object>();
	final static Map<String, Object> frontend_updateTimeList_map = new HashMap<String, Object>();
	static HashMap<String, ArrayList<String>> es_query_result_supplierANDkitchen = new HashMap<String, ArrayList<String>>();
	static HashMap<String, ArrayList<String>> es_query_result_food = new HashMap<String, ArrayList<String>>();
	static int es_query_limit = 10000;// ES查詢時候的比數限制最高10000
	private static final Logger logger = LogManager.getLogger(AFS_ini.class);

	// @SuppressWarnings("static-access")
	// public void querESfoodsafty(Calendar now, Calendar yestoday) throws
	// Exception {
	//
	// logger.info("querESfoodsafty");
	// SimpleDateFormat sdFormatES = new SimpleDateFormat("yyyy-MM-dd
	// HH:mm:ss");
	// SimpleDateFormat sdFormatFS = new SimpleDateFormat("yyyy/MM/dd");
	// String search_indexName = AFS_ini.searchedES_indexName;
	// String search_routing = now.get(Calendar.YEAR) + "_" +
	// now.get(Calendar.MONTH);
	// String st_date_es = sdFormatES.format(yestoday.getTime());
	// String sp_date_es = sdFormatES.format(now.getTime());
	// String st_date_fs = sdFormatFS.format(yestoday.getTime());
	// long start = Calendar.getInstance().getTimeInMillis();
	// es_query_result_supplierANDkitchen = es_DailyQuery(search_indexName,
	// search_routing, st_date_es, sp_date_es,
	// getClass().getClassLoader().getResource(AFS_ini.searchedES_dictionaryList[0]).getPath());
	// es_query_result_food = es_DailyQuery(search_indexName, search_routing,
	// st_date_es, sp_date_es,
	// getClass().getClassLoader().getResource(AFS_ini.searchedES_dictionaryList[1]).getPath());
	// Iterator<String> it =
	// es_query_result_supplierANDkitchen.keySet().iterator();
	// int data_count = es_query_result_supplierANDkitchen.keySet().size();
	// String[][] resultToAPI = new String[data_count][2];
	// while (it.hasNext()) {
	// Object rkey = it.next();
	// for (int i = 0; i < data_count; i++) {
	// if (es_query_result_supplierANDkitchen.get(rkey) != null) {
	// String tmp = es_query_result_supplierANDkitchen.get(rkey).toString();
	// resultToAPI[i][0] = tmp.substring(tmp.indexOf("[") + 1,
	// tmp.indexOf("]"));
	// }
	// if (es_query_result_food.get(rkey) != null) {
	// String tmp = es_query_result_food.get(rkey).toString();
	// resultToAPI[i][1] = tmp.substring(tmp.indexOf("[") + 1,
	// tmp.indexOf("]"));
	// }
	//
	// }
	// }
	// FoodSafety_API_Helper fahp = new FoodSafety_API_Helper();
	// es_query_result_supplierANDkitchen.clear();
	// es_query_result_food.clear();
	//
	// List<String> b1mf1m = new Common_Function()
	// .readFile(getClass().getClassLoader().getResource(AFS_ini.dailyQuery_dictionaryList[0]).getPath());
	// List<String> b3mf1m = new Common_Function()
	// .readFile(getClass().getClassLoader().getResource(AFS_ini.dailyQuery_dictionaryList[1]).getPath());
	// List<String> b7df7d = new Common_Function()
	// .readFile(getClass().getClassLoader().getResource(AFS_ini.dailyQuery_dictionaryList[2]).getPath());
	// for (int i = 0; i < data_count; i++) {
	// String[] tmp_food = new String[100];// not good
	// String[] tmp_sup = new String[100];
	// if (resultToAPI[i][1].contains(",")) {
	// tmp_food = resultToAPI[i][1].split(",");
	// } else {
	// tmp_food[0] = resultToAPI[i][1];
	// }
	// if (resultToAPI[i][0].contains(",")) {// null問題
	// tmp_sup = resultToAPI[i][0].split(",");
	// } else {
	// tmp_sup[0] = resultToAPI[i][0];
	// }
	// for (int k = 0; k < tmp_food.length; k++) {
	// String std = "";
	// String spd = "";
	// DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	// Date date = format.parse(st_date_fs);
	// Calendar c = Calendar.getInstance();
	// if (b1mf1m.contains(tmp_food[k])) {
	// c.setTime(date);
	// c.add(Calendar.MONTH, -1);
	// std = format.format(c.getTime());
	// c.setTime(date);
	// c.add(Calendar.MONTH, +1);
	// spd = format.format(c.getTime());
	// } else if (b3mf1m.contains(tmp_food[k])) {
	// c.setTime(date);
	// c.add(Calendar.MONTH, -3);
	// std = format.format(c.getTime());
	// c.setTime(date);
	// c.add(Calendar.MONTH, +1);
	// spd = format.format(c.getTime());
	// } else if (b7df7d.contains(tmp_food[k])) {
	// c.setTime(date);
	// c.add(Calendar.DATE, -7);
	// std = format.format(c.getTime());
	// c.setTime(date);
	// c.add(Calendar.DATE, +7);
	// spd = format.format(c.getTime());
	// } else {
	// c.setTime(date);
	// System.out.println(c.getTime());
	// c.add(Calendar.MONTH, -3);
	// std = format.format(c.getTime());
	// c.setTime(date);
	// c.add(Calendar.MONTH, +1);
	// spd = format.format(c.getTime());
	// }
	// for (int j = 0; j < tmp_sup.length; j++) {
	// if (tmp_sup[j] == null) {
	// continue;
	// }
	// fahp.sendPost(tmp_sup[j], tmp_food[k], std, spd);
	// }
	// }
	// }
	// logger.info("consume time:" + (Calendar.getInstance().getTimeInMillis() -
	// start));
	// }

	// @SuppressWarnings("static-access")
	// public static HashMap<String, ArrayList<String>> es_DailyQuery(String
	// indexName, String Rtime, String startDate,
	// String stopDate, String filePath) throws IOException {
	// HashMap<String, ArrayList<String>> result = new HashMap<String,
	// ArrayList<String>>();
	// if (!result.isEmpty()) {
	// result.clear();
	// logger.info("reulst clean");
	// }
	// HashSet<String> dic = new Common_Function().readDic(filePath);
	// Settings.builder().put("cluster.name", "FS-Cluster").build();
	// TransportClient client = new ES_helper().es_con();
	// for (String s : dic) {
	// SearchResponse response =
	// client.prepareSearch(indexName).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
	// .setQuery(QueryBuilders.termQuery("content", s)).setRouting(Rtime)
	// .setPostFilter(QueryBuilders.rangeQuery("updatetime").from(startDate).to(stopDate))
	// .setExplain(false).get();
	// if (response.getHits().getTotalHits() != 0) {
	// for (int i = 0; i < response.getHits().getTotalHits(); i++) {
	// if (result.get(response.getHits().getAt(i).getId()) != null) {
	// result.get(response.getHits().getAt(i).getId()).add(s);
	// } else {
	// ArrayList<String> myList = new ArrayList<String>();
	// myList.add(s);
	// result.put(response.getHits().getAt(i).getId(), myList);
	// }
	// }
	//
	// }
	// }
	// new ES_helper().es_close(client);
	// return result;
	// }

	public static ESResponse es_QueryUrl(String indexName, String estype) throws UnknownHostException, ParseException {
		ESResponse esr = new ESResponse();
		TransportClient client = new ES_helper().es_con();
		logger.info("es_QueryUrl");
		if (urlList_map.isEmpty() == false) {
			urlList_map.clear();
		}
		if (contentList_map.isEmpty() == false) {
			contentList_map.clear();
		}
		if (tagsList_map.isEmpty() == false) {
			tagsList_map.clear();
		}
		if (frontend_updateTimeList_map.isEmpty() == false) {
			frontend_updateTimeList_map.clear();
		}
		if ((Integer.valueOf(estype) < -1) || (Integer.valueOf(estype) > 4)) {
			esr.setStatus(false);
			return esr;
		}
		SearchResponse response;
		if (Integer.valueOf(estype) == 4) {
			String now = Common_Function.getDateTime();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = format.parse(now);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, -7);

			response = client.prepareSearch(AFS_ini.searchedES_indexName).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setQuery(QueryBuilders.termsQuery("tags", "1", "3"))
					.setPostFilter(QueryBuilders.rangeQuery("frontend_updatetime").from(format.format(c.getTime()))
							.to(Common_Function.getDateTime()))
					.addSort("frontend_updatetime", SortOrder.DESC).setSize(es_query_limit).setExplain(false).get();

		} else {
			response = client.prepareSearch(AFS_ini.searchedES_indexName).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setQuery(QueryBuilders.termQuery("tags", estype)).addSort("crawlTime", SortOrder.DESC)
					.setSize(es_query_limit).setExplain(false).get();
		}
		// System.out.println("@@@@@@@@@@@@@" +
		// response.getHits().getTotalHits());

		for (int i = 0; i < response.getHits().getTotalHits(); i++) {
			if (!response.getHits().getAt(i).getType().equals("gmail_tifsan")
					&& !response.getHits().getAt(i).getType().equals("gmail_lai")) {

				urlList_map.put(response.getHits().getAt(i).getId(),
						response.getHits().getAt(i).getSourceAsMap().get("url").toString());
			}
			tagsList_map.put(response.getHits().getAt(i).getId(),
					response.getHits().getAt(i).getSourceAsMap().get("tags").toString());
			if (!estype.equals("-1")) {
				frontend_updateTimeList_map.put(response.getHits().getAt(i).getId(),
						response.getHits().getAt(i).getSourceAsMap().get("frontend_updatetime").toString());
			}
			// System.out.println(response.getHits().getAt(i).getSourceAsMap().get("frontend_updatetime").toString());
			contentList_map.put(response.getHits().getAt(i).getId(),
					response.getHits().getAt(i).getSourceAsMap().get("content").toString());

		}
		esr.setUrlList(urlList_map);
		esr.setContentList(contentList_map);
		esr.setTagsList(tagsList_map);
		esr.setFrontend_updateTimeList(frontend_updateTimeList_map);
		return esr;

	}

	public ESResponse es_UpdateDatatoES(ESRequest req) throws UnknownHostException {

		logger.info("es_UpdateDatatoES");
		TransportClient client = new ES_helper().es_con();
		String tt = "";
		ESResponse esr = new ESResponse();
		if (!req.getUpdate_flag().isEmpty() && !req.getUpdate_id().isEmpty()) {

			SearchResponse response = client.prepareSearch(AFS_ini.searchedES_indexName)
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setQuery(QueryBuilders.queryStringQuery("_id:" + req.getUpdate_id())).setExplain(false).get();
			if (req.getUpdate_flag().equals("2")) {// 1正常,2待確認,3非食安,其他0
				tt = "2";
			} else if (req.getUpdate_flag().equals("1")) {
				tt = "1";
			} else if (req.getUpdate_flag().equals("3")) {
				tt = "3";
			} else if (req.getUpdate_flag().equals("0")) {
				tt = "1";
			} else {
				esr.setStatus(false);
				esr.setErr_msg("plz check the flag");
				return esr;

			}
			UpdateRequest updateRequest = new UpdateRequest();
			updateRequest.index(AFS_ini.searchedES_indexName);
			updateRequest.type(response.getHits().getAt(0).getType());
			updateRequest.id(req.getUpdate_id());
			updateRequest.routing(response.getHits().getAt(0).getField("_routing").getValue());
			try {

				updateRequest.doc(jsonBuilder().startObject().field("tags", tt)
						.field("tags_supplier", req.getUpdate_content_sup())
						.field("tags_food", req.getUpdate_content_food())
						.field("frontend_updatetime", Common_Function.getDateTime()).endObject());
				client.update(updateRequest).get();
				esr.setStatus(true);

			} catch (IOException | InterruptedException | ExecutionException e) {

				e.printStackTrace();
				esr.setErr_msg(e.getMessage());
				esr.setStatus(false);
			}

		} else {

			logger.info("please check id or flag");
			esr.setErr_msg("please check id or flag");
			esr.setStatus(false);
		}
		client.close();
		return esr;
	}

}
