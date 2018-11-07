package test.org.iii.ideas.foodsafety.rest;

import static org.junit.Assert.*;

import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.iii.ideas.foodsafety.rest.*;

public class ES_helperTest {
	ES_helper es;

	@Before
	public void setUp() throws Exception {
		es = new ES_helper();
	}

	@After
	public void tearDown() throws Exception {
		es = null;
	}

	@Test
	public void test_es_GetTypeById() throws UnknownHostException {
		assertEquals("苗栗縣衛生局", es.es_GetSiteNameById("AV3NhJGcpgF7uuMyXB6R"));
	}

	// @Test
	// public void test_es_query() throws UnknownHostException {
	// assertThat(es.es_query(AFS_ini.searchedES_indexName, "2015_12",
	// startDate, stopDate, filePath), IsMapContaining.hasKey("j"));
	// }

	@Test
	public void test_es_GetUrlById() throws UnknownHostException {
		assertEquals(
				"http://www.klchb.gov.tw/ch/news/newspaper/upt.aspx?pageno=8&key=&dateA=&dateB=&con=1&cid=0&fid=0&c0=640&p0=2549",
				es.es_GetUrlById("AV3NhI7KpgF7uuMyXB53"));
	}

}
