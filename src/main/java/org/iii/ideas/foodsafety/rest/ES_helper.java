package org.iii.ideas.foodsafety.rest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class ES_helper {
	private static final Logger logger = LogManager.getLogger(AFS_ini.class);

	public String es_GetSiteNameById(String id) throws UnknownHostException {
		logger.info("es_GetTypeById");
		ES_helper es = new ES_helper();
		TransportClient client = es.es_con();
		SearchResponse resp = client.prepareSearch(AFS_ini.searchedES_indexName)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(QueryBuilders.termQuery("_id", id))
				.setExplain(false).get();
		String sitename = resp.getHits().getAt(0).getSource().get("sitename").toString();
		if (sitename.equals("gmail_tifsan")) {
			sitename = "食品藥物安全通報系統";
		}
		logger.info("sitename:" + sitename);
		es.es_close(client);
		return sitename;
	}

	public String es_GetUrlById(String id) throws UnknownHostException {
		logger.info("es_GetUrlById");
		ES_helper es = new ES_helper();
		TransportClient client = es.es_con();
		SearchResponse resp = client.prepareSearch(AFS_ini.searchedES_indexName)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(QueryBuilders.termQuery("_id", id))
				.setExplain(false).get();
		String url = resp.getHits().getAt(0).getSource().get("url").toString();
		logger.info("Url:" + url);
		es.es_close(client);
		return url;
	}

	public String es_GetSourceTitleById(String id) throws UnknownHostException {
		logger.info("es_GetSourceTitleById");
		ES_helper es = new ES_helper();
		TransportClient client = es.es_con();
		SearchResponse resp = client.prepareSearch(AFS_ini.searchedES_indexName)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(QueryBuilders.termQuery("_id", id))
				.setExplain(false).get();
		String title = resp.getHits().getAt(0).getSource().get("title").toString();
		logger.info("title:" + title);
		es.es_close(client);
		return title;
	}

	@SuppressWarnings("resource")
	public TransportClient es_con() throws UnknownHostException {
		logger.info("es connect successful");
		Settings settings = Settings.builder().put("cluster.name", AFS_ini.elasticsearch_clusterName).build();
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(AFS_ini.elasticsearch_ip),
						AFS_ini.elasticsearch_port));
		return client;
	}

	public void es_close(Client client) {
		client.close();
	}
}
