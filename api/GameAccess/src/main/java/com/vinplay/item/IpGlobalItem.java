package com.vinplay.item;

public class IpGlobalItem implements java.io.Serializable{
	private static final long serialVersionUID = 7299440707840853896L;
	private long sip;
	private long eip;
	private String des;
	private String sips;
	private String eips;
	public long getSip() {
		return sip;
	}
	public void setSip(long sip) {
		this.sip = sip;
	}
	public long getEip() {
		return eip;
	}
	public void setEip(long eip) {
		this.eip = eip;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getSips() {
		return sips;
	}
	public void setSips(String sips) {
		this.sips = sips;
	}
	public String getEips() {
		return eips;
	}
	public void setEips(String eips) {
		this.eips = eips;
	}
	
}
