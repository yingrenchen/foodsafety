package test.org.iii.ideas.foodsafety.rest;

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.text.ParseException;

import org.iii.ideas.foodsafety.rest.AFS_ini;
import org.iii.ideas.foodsafety.rest.ESRequest;
import org.iii.ideas.foodsafety.rest.ESResponse;
import org.iii.ideas.foodsafety.rest.QueryfromES;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QueryfromEStTest {
	QueryfromES qs;

	@Before
	public void setUp() throws Exception {
		qs = new QueryfromES();
	}

	@After
	public void tearDown() throws Exception {
		qs = null;
	}

	@Test
	public void test_es_QueryUrl() throws UnknownHostException, ParseException {

		assertEquals(true, qs.es_QueryUrl(AFS_ini.searchedES_indexName, "-1").getStatus());
		assertEquals(true, qs.es_QueryUrl(AFS_ini.searchedES_indexName, "2").getStatus());
		assertEquals(true, qs.es_QueryUrl(AFS_ini.searchedES_indexName, "4").getStatus());
		assertEquals(false, qs.es_QueryUrl(AFS_ini.searchedES_indexName, "-2").getStatus());
		assertEquals(false, qs.es_QueryUrl(AFS_ini.searchedES_indexName, "5").getStatus());
	}

	@Test
	public void test_es_UpdateDatatoES() throws UnknownHostException, ParseException {
		ESRequest esr = new ESRequest();
		assertEquals(false, qs.es_UpdateDatatoES(esr).getStatus());
		esr.setUpdate_id("AV3NhI94pgF7uuMyXB6I");
		esr.setUpdate_flag("2");
		esr.setUpdate_content_food("test");
		esr.setUpdate_content_sup("test");
		assertEquals(true, qs.es_UpdateDatatoES(esr).getStatus());
		esr.setUpdate_flag("1");
		assertEquals(true, qs.es_UpdateDatatoES(esr).getStatus());
		esr.setUpdate_flag("3");
		assertEquals(true, qs.es_UpdateDatatoES(esr).getStatus());
		esr.setUpdate_flag("0");
		assertEquals(true, qs.es_UpdateDatatoES(esr).getStatus());
		esr.setUpdate_flag("-1");
		assertEquals(false, qs.es_UpdateDatatoES(esr).getStatus());
		esr.setUpdate_id("");
		assertEquals(false, qs.es_UpdateDatatoES(esr).getStatus());
	}

}
