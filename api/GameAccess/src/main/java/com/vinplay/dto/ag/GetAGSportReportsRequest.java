package com.vinplay.dto.ag;

import java.io.Serializable;

public class GetAGSportReportsRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 976124541306627951L;

	private String cagent;
	private String loginname;
	private String startdate;
	private String enddate;
	private String gametype;
	private String billno;
	private String thirdbillno;
	private String order;
	private String by; //Order by ASC or DESC
	private String page;
	private String perpage; //Default 100. Suggested max 500.
	
	public String getCagent() {
		return cagent;
	}
	
	public void setCagent(String cagent) {
		this.cagent = cagent;
	}
	
	public String getLoginname() {
		return loginname;
	}
	
	public void setLoginname(String loginname) {
		this.loginname = loginname;
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
	
	public String getBillno() {
		return billno;
	}
	
	public void setBillno(String billno) {
		this.billno = billno;
	}
	
	public String getThirdbillno() {
		return thirdbillno;
	}
	
	public void setThirdbillno(String thirdbillno) {
		this.thirdbillno = thirdbillno;
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
