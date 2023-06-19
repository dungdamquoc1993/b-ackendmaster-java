package com.vinplay.payment.entities;

public class BankConfig {
	/**
	 * @param key
	 * @param imageUrl
	 * @param name
	 * @param status
	 */
	public BankConfig(String key, String imageUrl, String name, Integer status, Integer isWithdraw) {
		super();
		this.key = key;
		this.imageUrl = imageUrl;
		this.name = name;
		this.status = status;
		this.isWithdraw = isWithdraw;
	}
	
	public BankConfig() {
		super();
	}

	private String key;
	private String imageUrl;
	private String name;
	private Integer status;
	private Integer isWithdraw;
	
	public Integer getIsWithdraw() {
		return isWithdraw;
	}

	public void setIsWithdraw(Integer isWithdraw) {
		this.isWithdraw = isWithdraw;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
