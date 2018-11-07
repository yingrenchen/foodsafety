package org.iii.ideas.foodsafety.rest;

public class ESRequest {
	private String update_content_food = "";
	private String update_content_sup = "";
	private String update_content_manufacture = "";
	private String update_content_kitchen = "";
	private String update_flag = "";
	private String update_id = "";
	private String survey_date;
	private String custom_survey_std;
	private String custom_survey_spd;

	public ESRequest() {
		this.update_id = "";
		this.update_content_food = "";
		this.update_content_sup = "";
		this.update_flag = "";
		this.survey_date = "";
		this.custom_survey_std = "";
		this.custom_survey_spd = "";

	}

	public void setUpdate_content_food(String update_content_food) {
		this.update_content_food = update_content_food;
	}

	public void setUpdate_flag(String update_flag) {
		this.update_flag = update_flag;
	}

	public void setCustom_survey_std(String custom_survey_std) {
		this.custom_survey_std = custom_survey_std;
	}

	public void setCustom_survey_spd(String custom_survey_spd) {
		this.custom_survey_spd = custom_survey_spd;
	}

	public void setSurvey_date(String survey_date) {
		this.survey_date = survey_date;
	}

	public void setUpdate_id(String update_id) {
		this.update_id = update_id;
	}

	public void setUpdate_content_sup(String update_content_sup) {
		this.update_content_sup = update_content_sup;
	}

	public void setUpdate_content_manufacture(String update_content_manufacture) {
		this.update_content_manufacture = update_content_manufacture;
	}

	public void setUpdate_content_kitchen(String update_content_kitchen) {
		this.update_content_kitchen = update_content_kitchen;
	}

	public String getCustom_survey_std() {
		return custom_survey_std;
	}

	public String getCustom_survey_spd() {
		return custom_survey_spd;
	}

	public String getSurvey_date() {
		return survey_date;
	}

	public String getUpdate_id() {
		return update_id;
	}

	public String getUpdate_content_food() {
		return update_content_food;
	}

	public String getUpdate_content_manufacture() {
		return update_content_manufacture;
	}

	public String getUpdate_content_kitchen() {
		return update_content_kitchen;
	}

	public String getUpdate_content_sup() {
		return update_content_sup;
	}

	public String getUpdate_flag() {
		return update_flag;
	}

}
