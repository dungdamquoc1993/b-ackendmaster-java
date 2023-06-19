package com.vinplay.payment.entities;

import java.util.List;

public class Config {

	private String requestAPI;
	private String statusAPI;
	private String merchantCode;
	private String merchantKey;
	private String currencyCode;
	private String returnUrl;
	private String notifyUrl;
	private String notifyWithdrawUrl;
	private List<PayType> payTypes;
	private List<BankConfig> banks;
	private Integer minMoney;
	private Integer status;

	/**
	 * @param requestAPI
	 * @param statusAPI
	 * @param merchantCode
	 * @param merchantKey
	 * @param currencyCode
	 * @param returnUrl
	 * @param notifyUrl
	 * @param payTypes
	 * @param banks
	 * @param minMoney
	 * @param status
	 */
	public Config(String requestAPI, String statusAPI, String merchantCode, String merchantKey, String currencyCode,
			String returnUrl, String notifyUrl, List<PayType> payTypes, List<BankConfig> banks, Integer minMoney,
			Integer status) {
		super();
		this.requestAPI = requestAPI;
		this.statusAPI = statusAPI;
		this.merchantCode = merchantCode;
		this.merchantKey = merchantKey;
		this.currencyCode = currencyCode;
		this.returnUrl = returnUrl;
		this.notifyUrl = notifyUrl;
		this.payTypes = payTypes;
		this.banks = banks;
		this.minMoney = minMoney;
		this.status = status;
	}

	public Config() {
		super();
	}
	
	public String getNotifyWithdrawUrl() {
		return notifyWithdrawUrl;
	}

	public void setNotifyWithdrawUrl(String notifyWithdrawUrl) {
		this.notifyWithdrawUrl = notifyWithdrawUrl;
	}

	public List<PayType> getPayTypes() {
		return payTypes;
	}

	public void setPayTypes(List<PayType> payTypes) {
		this.payTypes = payTypes;
	}

	public String getRequestAPI() {
		return requestAPI;
	}

	public void setRequestAPI(String requestAPI) {
		this.requestAPI = requestAPI;
	}

	public String getStatusAPI() {
		return statusAPI;
	}

	public void setStatusAPI(String statusAPI) {
		this.statusAPI = statusAPI;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getMerchantKey() {
		return merchantKey;
	}

	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public List<PayType> getPayType() {
		return payTypes;
	}

	public void setPayType(List<PayType> payType) {
		this.payTypes = payType;
	}

	public List<BankConfig> getBanks() {
		return banks;
	}

	public void setBanks(List<BankConfig> banks) {
		this.banks = banks;
	}

	public Integer getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(Integer minMoney) {
		this.minMoney = minMoney;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
