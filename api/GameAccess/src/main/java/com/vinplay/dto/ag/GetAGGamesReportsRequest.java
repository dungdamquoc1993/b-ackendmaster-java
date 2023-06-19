package com.vinplay.dto.ag;

import java.io.Serializable;

public class GetAGGamesReportsRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 236400760919245322L;

	private String cagent;
	private String startdate;
	private String enddate; //Every data request max 10 minutes
	private String gametype;
	private String order;
	private String by; //Sort ASC or DESC
	private String page;
	private String perpage; // Number of records per page. Default 100. Suggested <= 500.
	
	public String getCagent() {
		return cagent;
	}
	public void setCagent(String cagent) {
		this.cagent = cagent;
	}
	
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	
	public String getGametype() {
		return gametype;
	}
	public void setGametype(String gametype) {
		this.gametype = gametype;
	}
	
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
	public String getBy() {
		return by;
	}
	public void setBy(String by) {
		this.by = by;
	}
	
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	
	public String getPerpage() {
		return perpage;
	}
	public void setPerpage(String perpage) {
		this.perpage = perpage;
	}
}
