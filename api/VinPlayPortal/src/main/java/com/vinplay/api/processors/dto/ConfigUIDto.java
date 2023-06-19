package com.vinplay.api.processors.dto;

import java.util.List;

import com.vinplay.payment.entities.BankConfig;
import com.vinplay.payment.entities.Config;
import com.vinplay.payment.entities.PayType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ConfigUIDto{
	
	private String currencyCode;
	private List<PayType> payType;
	private List<BankConfig> banks;
	private Integer minMoney;
	private Integer status;

	public String getCurrencyCode() {
		return currencyCode;
	}
	
	public ConfigUIDto(Config config) {
		this.currencyCode = config.getCurrencyCode();
		this.payType = config.getPayType();
		this.banks = config.getBanks();
		this.minMoney = config.getMinMoney();
		this.status = config.getStatus();
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public List<PayType> getPayType() {
		return payType;
	}

	public void setPayType(List<PayType> payType) {
		this.payType = payType;
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

	public ConfigUIDto() {
		super();
	}

	public ConfigUIDto(String currencyCode, List<PayType> payType, List<BankConfig> banks, Integer minMoney,
			Integer status) {
		super();
		this.currencyCode = currencyCode;
		this.payType = payType;
		this.banks = banks;
		this.minMoney = minMoney;
		this.status = status;
	}

	public String toString() {
		ObjectWriter ow = new ObjectMapper().writer();
		ow.with(SerializationFeature.INDENT_OUTPUT);
		try {
			String json = ow.writeValueAsString(this);
			return json;
		} catch (Exception e) {
			return null;
		}
	}
}
