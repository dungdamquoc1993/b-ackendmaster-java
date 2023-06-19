package com.vinplay.item;

import java.io.Serializable;

public class AutoGenNumberItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4061310514779767492L;
	private String loginname;
	private int usertype;
	private int lotteryid;
	private String issue;
	private String content;
	private String datetime;
	private String param1;
	private String param2;
	private String param3;
	private int tindex;
	
	public AutoGenNumberItem() {
		super();
	}


	


	public AutoGenNumberItem(String loginname, int usertype, int lotteryid, String issue, String content,
			String datetime, String param1, String param2, String param3, int tindex) {
		super();
		this.loginname = loginname;
		this.usertype = usertype;
		this.lotteryid = lotteryid;
		this.issue = issue;
		this.content = content;
		this.datetime = datetime;
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
		this.tindex = tindex;
	}





	public String getLoginname() {
		return loginname;
	}


	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}


	public int getUsertype() {
		return usertype;
	}


	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}


	public int getLotteryid() {
		return lotteryid;
	}


	public void setLotteryid(int lotteryid) {
		this.lotteryid = lotteryid;
	}


	public String getIssue() {
		return issue;
	}


	public void setIssue(String issue) {
		this.issue = issue;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getDatetime() {
		return datetime;
	}


	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}


	public String getParam1() {
		return param1;
	}


	public void setParam1(String param1) {
		this.param1 = param1;
	}


	public String getParam2() {
		return param2;
	}


	public void setParam2(String param2) {
		this.param2 = param2;
	}


	public String getParam3() {
		return param3;
	}


	public void setParam3(String param3) {
		this.param3 = param3;
	}


	public int getTindex() {
		return tindex;
	}


	public void setTindex(int tindex) {
		this.tindex = tindex;
	}

	
}
