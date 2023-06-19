package com.vinplay.item;

import java.io.Serializable;

public class TDUser implements Serializable{
	private static final long serialVersionUID = -1894335728583841976L;
	private String tdid;
	private String loginname;
	private String password;
	private int max_transfer;
	private int min_transfer;
	private int tdcountid;
	public String getTdid() {
		return tdid;
	}
	public void setTdid(String tdid) {
		this.tdid = tdid;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getMax_transfer() {
		return max_transfer;
	}
	public void setMax_transfer(int max_transfer) {
		this.max_transfer = max_transfer;
	}
	public int getMin_transfer() {
		return min_transfer;
	}
	public void setMin_transfer(int min_transfer) {
		this.min_transfer = min_transfer;
	}
	public int getTdcountid() {
		return tdcountid;
	}
	public void setTdcountid(int tdcountid) {
		this.tdcountid = tdcountid;
	}
	
	
}
