package com.vinplay.dto.ebet;

import java.io.Serializable;

public class TransactionsDto implements Serializable{
	private static final long serialVersionUID = -7154659234100445973L;
	private Integer userId;
	private String username;
	private Integer subChannelId;
	private Integer tradeType;
	private String roundNo;
	private Double transBeforeBalance;
	private Double transBalance;
	private Double transAfterBalance;
	private String time;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getSubChannelId() {
		return subChannelId;
	}
	public void setSubChannelId(Integer subChannelId) {
		this.subChannelId = subChannelId;
	}
	public Integer getTradeType() {
		return tradeType;
	}
	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}
	public String getRoundNo() {
		return roundNo;
	}
	public void setRoundNo(String roundNo) {
		this.roundNo = roundNo;
	}
	public Double getTransBeforeBalance() {
		return transBeforeBalance;
	}
	public void setTransBeforeBalance(Double transBeforeBalance) {
		this.transBeforeBalance = transBeforeBalance;
	}
	public Double getTransBalance() {
		return transBalance;
	}
	public void setTransBalance(Double transBalance) {
		this.transBalance = transBalance;
	}
	public Double getTransAfterBalance() {
		return transAfterBalance;
	}
	public void setTransAfterBalance(Double transAfterBalance) {
		this.transAfterBalance = transAfterBalance;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

}
