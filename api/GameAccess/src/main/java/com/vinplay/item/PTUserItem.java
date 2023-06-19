package com.vinplay.item;

import java.io.Serializable;

public class PTUserItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8478752055477313435L;

	private String ptid;

	private String loginname;

	private String password;

	private int ptcountid;
	
	private String server;

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getPtid() {
		return ptid;
	}

	public void setPtid(String ptid) {
		this.ptid = ptid;
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

	public int getPtcountid() {
		return ptcountid;
	}

	public void setPtcountid(int ptcountid) {
		this.ptcountid = ptcountid;
	}
}
