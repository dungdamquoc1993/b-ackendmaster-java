package com.vinplay.dto.cmd;

import java.io.Serializable;

public class CmdLaunchGameResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -399397457065761063L;

	private String token;
	private String webRoot;
	private String mobileRoot;
	private String newMobileRoot;
	private String userName;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getWebRoot() {
		return webRoot;
	}

	public void setWebRoot(String webRoot) {
		this.webRoot = webRoot;
	}

	public String getMobileRoot() {
		return mobileRoot;
	}

	public void setMobileRoot(String mobileRoot) {
		this.mobileRoot = mobileRoot;
	}

	public String getNewMobileRoot() {
		return newMobileRoot;
	}

	public void setNewMobileRoot(String newMobileRoot) {
		this.newMobileRoot = newMobileRoot;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
