package com.vinplay.item;

import java.io.Serializable;

public class SAUser implements Serializable{
	private static final long serialVersionUID = -4875777687211280900L;
	private String said;
	private String loginname;
	private int sacountid;
	
	public SAUser() {
		super();
	}
	public SAUser(String said, String loginname) {
		super();
		this.said = said;
		this.loginname = loginname;
	}
	public String getSaid() {
		return said;
	}
	public void setSaid(String said) {
		this.said = said;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public int getSacountid() {
		return sacountid;
	}
	public void setSacountid(int sacountid) {
		this.sacountid = sacountid;
	}
	
	
}
