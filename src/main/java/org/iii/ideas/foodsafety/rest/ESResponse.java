package org.iii.ideas.foodsafety.rest;

public class ESResponse {
	private Boolean status;
	private String err_msg = "";
	private Object urlList;
	private Object contentList;
	private Object tagsList;
	private Object frontend_updateTimeList;

	public ESResponse() {
		this.status = true;
		this.err_msg = "";
		this.urlList = null;
		this.contentList = null;
		this.tagsList = null;
		this.frontend_updateTimeList = null;
	}

	public boolean getStatus() {
		return status;
	}

	public String getErr_msg() {
		return err_msg;
	}

	public Object getTagsListList() {
		return tagsList;
	}

	public Object getFrontend_updateTimeList() {
		return frontend_updateTimeList;
	}

	public Object getUrlList() {
		return urlList;
	}

	public Object getContentList() {
		return contentList;
	}

	public void setTagsList(Object tagsList) {
		this.tagsList = tagsList;
	}

	public void setFrontend_updateTimeList(Object frontend_updateTimeList) {
		this.frontend_updateTimeList = frontend_updateTimeList;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public void setUrlList(Object urlList) {
		this.urlList = urlList;
	}

	public void setContentList(Object contentList) {
		this.contentList = contentList;
	}

}
