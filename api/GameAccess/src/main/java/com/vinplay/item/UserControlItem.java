package com.vinplay.item;

import java.io.Serializable;
import java.util.Date;

public class UserControlItem implements Serializable{

	private static final long serialVersionUID = 1218757629698610487L;

	private String userName;
	
	private Date lastInvokeTime;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getLastInvokeTime() {
		return lastInvokeTime;
	}

	public void setLastInvokeTime(Date lastInvokeTime) {
		this.lastInvokeTime = lastInvokeTime;
	}

}
