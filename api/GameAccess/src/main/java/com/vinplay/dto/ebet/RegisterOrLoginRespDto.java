package com.vinplay.dto.ebet;

import java.io.Serializable;

public class RegisterOrLoginRespDto  implements Serializable{
	private static final long serialVersionUID = 2557855854199928599L;
	private Integer status;
  private Integer subChannelId;
  private String nickname;
  private String accessToken;
  private String username;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSubChannelId() {
		return subChannelId;
	}
	public void setSubChannelId(Integer subChannelId) {
		this.subChannelId = subChannelId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
