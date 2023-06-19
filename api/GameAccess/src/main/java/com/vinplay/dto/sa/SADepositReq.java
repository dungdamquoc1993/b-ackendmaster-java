package com.vinplay.dto.sa;

import java.io.Serializable;

public class SADepositReq extends BaseRequestDto implements Serializable{

	private static final long serialVersionUID = 5400080070938357926L;
	private String method;
	private String key;
	private String time;
	private String username;
	private String orderid;
	private String creditamount;
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
	public String getCreditamount() {
		return creditamount;
	}
	public void setCreditamount(String creditamount) {
		this.creditamount = creditamount;
	}
	
	
}
