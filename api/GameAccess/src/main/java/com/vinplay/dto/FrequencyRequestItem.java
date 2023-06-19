package com.vinplay.dto;

public class FrequencyRequestItem {
	private long time;
	private String loginname;
	public FrequencyRequestItem(String loginname) {
		time = new java.util.Date().getTime();
		this.loginname = loginname;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "FrequencyRequestItem [time=" + time + ", loginname=" + loginname + "]";
	}
	
}
