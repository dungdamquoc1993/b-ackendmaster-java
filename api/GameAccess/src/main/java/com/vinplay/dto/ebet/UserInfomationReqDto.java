package com.vinplay.dto.ebet;

import java.io.Serializable;

public class UserInfomationReqDto extends BaseReqDto implements Serializable{
	private static final long serialVersionUID = 6186007644849414644L;
	private String username;
	private String signature;
	private int timestamp;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

}
