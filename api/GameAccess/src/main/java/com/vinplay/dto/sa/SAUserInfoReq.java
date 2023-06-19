package com.vinplay.dto.sa;

import java.io.Serializable;

public class SAUserInfoReq extends BaseRequestDto implements Serializable{

	private static final long serialVersionUID = -6649014638629078162L;
	private String method;
	private String key;
	private String time;
	private String username;
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	

}
