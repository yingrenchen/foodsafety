package org.iii.ideas.foodsafety.rest;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest API進入點,接收JSON物件後使用Java reflection呼叫相對應的服務執行並將處理結果回傳
 */
@RestController
public class RestProcessor {
	private static final Logger logger = LogManager.getLogger(AFS_ini.class);
	QueryfromES qs = new QueryfromES();

	@RequestMapping(value = "/urlList", method = RequestMethod.GET)
	public @ResponseBody ESResponse humanwork_URLlist(@RequestParam(value = "estype", required = true) String estype)
			throws Exception {
		logger.info("humanwork_URLlist,estype:" + estype);
		return QueryfromES.es_QueryUrl(AFS_ini.searchedES_indexName, estype);
	}

	@RequestMapping(value = "/updateANDquery", method = RequestMethod.POST)
	public @ResponseBody ESResponse humanwork_UpdateANDquery(@RequestBody ESRequest req)
			throws IOException, InterruptedException, ExecutionException {
		logger.info("humanwork_UpdateANDquery");
		logger.info("SurveyDate:" + req.getSurvey_date());
		logger.info("Content_Food:" + req.getUpdate_content_food());
		logger.info("Content_Sup:" + req.getUpdate_content_sup());
		logger.info("Content_Manufactur:" + req.getUpdate_content_manufacture());
		logger.info("Content_Kitchen" + req.getUpdate_content_kitchen());
		logger.info("_flag:" + req.getUpdate_flag());
		logger.info("_id:" + req.getUpdate_id());
		ESResponse esr = new ESResponse();
		FoodSafety_API_Helper fahp = new FoodSafety_API_Helper();
		ES_helper es = new ES_helper();
		try {
			if (req.getUpdate_id().isEmpty()) {
				esr.setStatus(false);
				esr.setErr_msg("plz enter data id");
				return esr;
			} else if (req.getUpdate_flag().isEmpty()) {
				esr.setStatus(false);
				esr.setErr_msg("plz enter _flag");
				return esr;
			}
			if (req.getUpdate_flag().equals("1")) {
				if (req.getUpdate_content_food().isEmpty()) {
					esr.setStatus(false);
					esr.setErr_msg("plz enter food name");
					return esr;
				} else if (req.getUpdate_content_sup().isEmpty()) {
					esr.setStatus(false);
					esr.setErr_msg("plz enter supplier name");
					return esr;
				} else if (req.getSurvey_date().isEmpty()) {
					esr.setStatus(false);
					esr.setErr_msg("plz enter date");
					return esr;
				}
				if (!req.getCustom_survey_std().isEmpty() || !req.getCustom_survey_spd().isEmpty()) {
					if (!req.getCustom_survey_spd().isEmpty() && !req.getCustom_survey_std().isEmpty()) {
						fahp.sendPost(req.getUpdate_content_sup(), req.getUpdate_content_kitchen(),
								req.getUpdate_content_manufacture(), req.getUpdate_content_food(),
								req.getCustom_survey_std(), req.getCustom_survey_spd(),
								es.es_GetSiteNameById(req.getUpdate_id()), es.es_GetSourceTitleById(req.getUpdate_id()),
								es.es_GetUrlById(req.getUpdate_id()), req.getSurvey_date());
					} else {
						esr.setStatus(false);
						esr.setErr_msg("enter date");
						return esr;
					}
				} else {
					fahp.checkSurveyDate(req.getUpdate_content_food(), req.getUpdate_content_sup(),req.getUpdate_content_manufacture(),req.getUpdate_content_kitchen(),
							req.getSurvey_date(), req.getUpdate_id(), req.getSurvey_date());
				}
			}
			logger.info("Update data to ES");
			QueryfromES qs = new QueryfromES();
			esr = qs.es_UpdateDatatoES(req);
			esr.setStatus(true);

		} catch (Exception e) {
			logger.error(e.getMessage());
			esr.setErr_msg(e.getMessage());
			esr.setStatus(false);
		}
		return esr;
	}

}
