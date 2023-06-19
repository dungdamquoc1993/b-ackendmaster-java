package com.vinplay.item;

public class BankStatementLogItem implements java.io.Serializable{

	private static final long serialVersionUID = 6181959866909041301L;
	private String bstmid;
	private String optby;
	private String opttime;
	private String remark;
	private String ip;
	public String getBstmid() {
		return bstmid;
	}
	public void setBstmid(String bstmid) {
		this.bstmid = bstmid;
	}
	public String getOptby() {
		return optby;
	}
	public void setOptby(String optby) {
		this.optby = optby;
	}
	public String getOpttime() {
		return opttime;
	}
	public void setOpttime(String opttime) {
		this.opttime = opttime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
}
