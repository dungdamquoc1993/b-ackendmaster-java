package com.vinplay.dto.sa;

import java.io.Serializable;

public class SADepositResp extends BaseResponseDto implements Serializable{

	private static final long serialVersionUID = 8838327582218309443L;
	private String username;
	private double balance;
	private double creditamount;
	private String orderid;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getCreditamount() {
		return creditamount;
	}
	public void setCreditamount(double creditamount) {
		this.creditamount = creditamount;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	
	
}
