package com.vinplay.dto.ebet;

import java.io.Serializable;

public class UserInfomationRespDto extends BaseRespDto implements Serializable {
	
	/**
	 * 
	 */
	private String username;
	private String channelId;
	private Integer subChannelId;
	private Integer userId;
	private Double money;
	private Integer timestamp;
	private WalletDto[] wallet;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
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

	public WalletDto[] getWallet() {
		return wallet;
	}

	public void setWallet(WalletDto[] wallet) {
		this.wallet = wallet;
	}

}
