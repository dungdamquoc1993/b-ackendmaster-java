package com.vinplay.dto.sa;

import java.io.Serializable;

public class SAWithdrawalReq extends BaseRequestDto implements Serializable{
	private static final long serialVersionUID = -1397393638455066627L;
	private String method;
	private String key;
	private String time;
	private double debitamount;
	private String username;
	private String orderid;
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public double getDebitamount() {
		return debitamount;
	}
	public void setDebitamount(double debitamount) {
		this.debitamount = debitamount;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	
	
	
}
