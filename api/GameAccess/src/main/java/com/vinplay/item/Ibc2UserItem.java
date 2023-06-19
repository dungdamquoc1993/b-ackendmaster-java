package com.vinplay.item;


public class Ibc2UserItem implements java.io.Serializable{
	private static final long serialVersionUID = 1518215517444581248L;
	private String ibcid;
	private int ibccountid;
	private String loginname=null;
	private double max_transfer;
	private double min_transfer;

	public String getIbcid() {
		return ibcid;
	}

	public void setIbcid(String ibcid) {
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

	public int getIbccountid() {
		return ibccountid;
	}

	public void setIbccountid(int ibccountid) {
		this.ibccountid = ibccountid;
	}

	public Ibc2UserItem(String ibcid, String loginname, double max_transfer, double min_transfer) {
		this.ibcid = ibcid;
		this.loginname = loginname;
		this.max_transfer = max_transfer;
		this.min_transfer = min_transfer;
	}

	public Ibc2UserItem() {
	}
	
}
