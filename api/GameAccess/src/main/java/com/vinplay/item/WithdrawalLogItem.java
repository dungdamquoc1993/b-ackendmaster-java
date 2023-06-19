package com.vinplay.item;

public class WithdrawalLogItem implements java.io.Serializable{
	private static final long serialVersionUID = 5173707471366743199L;
	private String wid;
	private String optby;
	private String opttime;
	private String remark;
	private String ip;
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
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
