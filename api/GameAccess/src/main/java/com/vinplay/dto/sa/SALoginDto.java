package com.vinplay.dto.sa;

import java.io.Serializable;

public class SALoginDto extends BaseRequestDto implements Serializable{
	private static final long serialVersionUID = 5011544748213689050L;
	private String loginname;
	private String said;
	private String username;
	private String token;
	private String url;
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getSaid() {
		return said;
	}
	public void setSaid(String said) {
		this.said = said;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
	
}
