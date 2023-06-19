package com.vinplay.item;


public class UserSameDeviceItem implements java.io.Serializable{
	private static final long serialVersionUID = 8260964652575033866L;
	private int id;
	private String loginnamestr;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLoginnamestr() {
		return loginnamestr;
	}

	public void setLoginnamestr(String loginnamestr) {
		this.loginnamestr = loginnamestr;
	}
}
