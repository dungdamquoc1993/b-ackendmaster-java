package com.vinplay.item;

import java.io.Serializable;

public class AGUserItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9024592324210751432L;

	private String agid;

	private String loginname;

	private String password;

	private int agcountid;

	public String getAgid() {
		return agid;
	}

	public void setAgid(String agid) {
		this.agid = agid;
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

	public int getAgcountid() {
		return agcountid;
	}

	public void setAgcountid(int agcountid) {
		this.agcountid = agcountid;
	}
}
