package com.vinplay.dto.ibc2.common;

import java.io.Serializable;

public class CheckBalanceDataRespDto implements Serializable{
	private static final long serialVersionUID = -6990363131592792881L;
	private String vendor_member_id;
	private Double balance;
	private Double outstanding;
	private Integer currency;
	private Integer error_code;
	
	public String getVendor_member_id() {
		return vendor_member_id;
	}
	public void setVendor_member_id(String vendor_member_id) {
		this.vendor_member_id = vendor_member_id;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getOutstanding() {
		return outstanding;
	}
	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	public Integer getError_code() {
		return error_code;
	}
	public void setError_code(Integer error_code) {
		this.error_code = error_code;
	}
}
