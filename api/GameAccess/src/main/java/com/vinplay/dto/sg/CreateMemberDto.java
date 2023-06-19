package com.vinplay.dto.sg;

import java.io.Serializable;

public class CreateMemberDto extends BaseSGRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String currency;
	private String betLimit;
	private String language;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBetLimit() {
		return betLimit;
	}

	public void setBetLimit(String betLimit) {
		this.betLimit = betLimit;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
