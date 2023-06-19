package com.vinplay.item;

public class LoginItem implements java.io.Serializable{
	private static final long serialVersionUID = 5859438108636023987L;
	private String loginname;
	private String loginip;
	private String logintime;
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getLoginip() {
		return loginip;
	}
	public void setLoginip(String loginip) {
		this.loginip = loginip;
	}
	public String getLogintime() {
		return logintime;
	}
	public void setLogintime(String logintime) {
		this.logintime = logintime;
	}
}
