package com.vinplay.dto.sa;

import java.io.Serializable;

public class SAWithdrawalResp extends BaseResponseDto implements Serializable{
	private static final long serialVersionUID = -1629785012786959344L;
	private String username;
	private double balance;
	private double debitamount;
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
	public double getDebitamount() {
		return debitamount;
	}
	public void setDebitamount(double debitamount) {
		this.debitamount = debitamount;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	
	

}
