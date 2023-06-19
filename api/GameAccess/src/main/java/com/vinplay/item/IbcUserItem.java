package com.vinplay.item;


public class IbcUserItem implements java.io.Serializable{
	private static final long serialVersionUID = 7595129257618409588L;
	private int ibcid;
	private String loginname=null;
	private double max_transfer;
	private double min_transfer;
	
	
	public int getIbcid() {
		return ibcid;
	}
	public void setIbcid(int ibcid) {
		this.ibcid = ibcid;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public double getMax_transfer() {
		return max_transfer;
	}
	public void setMax_transfer(double max_transfer) {
		this.max_transfer = max_transfer;
	}
	public double getMin_transfer() {
		return min_transfer;
	}
	public void setMin_transfer(double min_transfer) {
		this.min_transfer = min_transfer;
	}
}
