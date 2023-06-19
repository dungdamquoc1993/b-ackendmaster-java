package com.vinplay.dto.ebet;

import java.io.Serializable;

public class UserMoneyReqDto extends BaseReqDto implements Serializable{
	private static final long serialVersionUID = -4946962234222359738L;
	private String username;
	private String signature;
	
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

}
