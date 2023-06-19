package com.vinplay.dto.cmd;

import java.io.Serializable;

public class CmdLoginBase64 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7408940588240960274L;

	private String loginName;
	private String sportsbook_userName;
	private String secret_key;
	private long timeStamp;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getSportsbook_userName() {
		return sportsbook_userName;
	}

	public void setSportsbook_userName(String sportsbook_userName) {
		this.sportsbook_userName = sportsbook_userName;
	}

	public String getSecret_key() {
		return secret_key;
	}

	public void setSecret_key(String secret_key) {
		this.secret_key = secret_key;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

}
