package com.vinplay.dto.sg;

import java.io.Serializable;

public class UpdateBetLimitDto extends BaseSGRequest implements Serializable{
	static final long serialVersionUID = 1L;
	private String userId;
	private String betLimit;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBetLimit() {
		return betLimit;
	}
	public void setBetLimit(String betLimit) {
		this.betLimit = betLimit;
	}
	
}
