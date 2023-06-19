package com.vinplay.item;

public class IPItem implements java.io.Serializable{
	private static final long serialVersionUID = -2343930905231321309L;
	private String ip;
	private long ips;
	private int times;
	private String des;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public long getIps() {
		return ips;
	}
	public void setIps(long ips) {
		this.ips = ips;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
}
