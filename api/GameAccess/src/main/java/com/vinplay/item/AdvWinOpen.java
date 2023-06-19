package com.vinplay.item;

public class AdvWinOpen implements java.io.Serializable{
	private static final long serialVersionUID = -6739400954998915646L;
	private Integer lotteryid = 1;
	private String id = "1";
	private String loginname = "";
	/**
	 * 比例 or 固定号码
	 */
	private String type = "";
	
	private String winnumber = "";
	private String s1 = "";
	private String s2="";
	public Integer getLotteryid() {
		return lotteryid;
	}
	public void setLotteryid(Integer lotteryid) {
		this.lotteryid = lotteryid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWinnumber() {
		return winnumber;
	}
	public void setWinnumber(String winnumber) {
		this.winnumber = winnumber;
	}
	public String getS1() {
		return s1;
	}
	public void setS1(String s1) {
		this.s1 = s1;
	}
	public String getS2() {
		return s2;
	}
	public void setS2(String s2) {
		this.s2 = s2;
	}
}
