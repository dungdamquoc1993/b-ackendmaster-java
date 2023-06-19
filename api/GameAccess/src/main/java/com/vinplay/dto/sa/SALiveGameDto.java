package com.vinplay.dto.sa;

import java.io.Serializable;

public class SALiveGameDto extends BaseRequestDto implements Serializable{
	private static final long serialVersionUID = -7454213480431922695L;
	private String username;
	private String token;
	private String lobby;
	private String lang;
	private String returnurl;
	private String mobile;
	private String h5web;
	private String options;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getLobby() {
		return lobby;
	}
	public void setLobby(String lobby) {
		this.lobby = lobby;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getReturnurl() {
		return returnurl;
	}
	public void setReturnurl(String returnurl) {
		this.returnurl = returnurl;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getH5web() {
		return h5web;
	}
	public void setH5web(String h5web) {
		this.h5web = h5web;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	
	
}
