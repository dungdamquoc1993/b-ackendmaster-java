package com.vinplay.dto.esport;

import java.io.Serializable;

public class EsportLoginBase64 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4427821141597983043L;
	/**
	 * 
	 */
	private String loginName;
	private String esport_userName;
	private String secret_key;
	private long timeStamp;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getEsport_userName() {
		return esport_userName;
	}

	public void setEsport_userName(String esport_userName) {
		this.esport_userName = esport_userName;
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

	public EsportLoginBase64(String loginName, String esport_userName, String secret_key, long timeStamp) {
		super();
		this.loginName = loginName;
		this.esport_userName = esport_userName;
		this.secret_key = secret_key;
		this.timeStamp = timeStamp;
	}

	public EsportLoginBase64() {
		super();
	}

	@Override
	public String toString() {
		return "EsportLoginBase64 [loginName=" + loginName + ", esport_userName=" + esport_userName + ", secret_key="
				+ secret_key + ", timeStamp=" + timeStamp + "]";
	}

}
