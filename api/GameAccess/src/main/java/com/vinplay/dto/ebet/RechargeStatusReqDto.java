package com.vinplay.dto.ebet;

import java.io.Serializable;

public class RechargeStatusReqDto extends BaseReqDto implements Serializable{
	private static final long serialVersionUID = -2370744360250754694L;
	private String rechargeReqId;
	private String signature;
	
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

}
