package com.vinplay.dto.ebet;

import java.io.Serializable;

public class RechargeStatusRespDto extends BaseRespDto implements Serializable{
	private static final long serialVersionUID = -2549553835565775700L;
	private String rechargeReqId;
	
	public String getRechargeReqId() {
		return rechargeReqId;
	}
	public void setRechargeReqId(String rechargeReqId) {
		this.rechargeReqId = rechargeReqId;
	}

}
