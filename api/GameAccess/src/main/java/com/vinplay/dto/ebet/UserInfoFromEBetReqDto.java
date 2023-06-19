package com.vinplay.dto.ebet;

import java.io.Serializable;

public class UserInfoFromEBetReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 3230857510636901094L;
	private String cmd;
	private String username;
	private Integer channelId;
	private Integer subChannelId;
	private Integer userId;
	private Double money;
	private Integer timestamp;
	private String ip;
	private String signature;
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	public Integer getSubChannelId() {
		return subChannelId;
	}
	public void setSubChannelId(Integer subChannelId) {
		this.subChannelId = subChannelId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Integer getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}

}
