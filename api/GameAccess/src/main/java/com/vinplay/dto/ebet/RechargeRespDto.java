package com.vinplay.dto.ebet;

import java.io.Serializable;

public class RechargeRespDto extends BaseRespDto implements Serializable{
	private static final long serialVersionUID = -998018547120990897L;
	private Double money;
	private String rechargeReqId;
	
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

}
