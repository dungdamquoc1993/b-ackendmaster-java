package com.vinplay.dto.ebet;

import java.io.Serializable;

public class CreateUserReqDto extends BaseReqDto implements Serializable{
	private static final long serialVersionUID = 3413979439864443519L;
	private String username;
	private Integer lang;
	private String signature;
	private Integer subChannelId;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getLang() {
		return lang;
	}
	public void setLang(Integer lang) {
		this.lang = lang;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public Integer getSubChannelId() {
		return subChannelId;
	}
	public void setSubChannelId(Integer subChannelId) {
		this.subChannelId = subChannelId;
	}
}
