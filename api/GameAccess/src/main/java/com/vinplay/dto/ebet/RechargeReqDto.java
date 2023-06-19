package com.vinplay.dto.ebet;

import java.io.Serializable;

public class RechargeReqDto extends BaseReqDto implements Serializable{
	private static final long serialVersionUID = 7387727750878698322L;
	private String username;
	private Double money;
	private String rechargeReqId;
	private String signature;
	private Integer timestamp;
	private Integer typeId;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getRechargeReqId() {
		return rechargeReqId;
	}
	public void setRechargeReqId(String rechargeReqId) {
		this.rechargeReqId = rechargeReqId;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public Integer getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

}
