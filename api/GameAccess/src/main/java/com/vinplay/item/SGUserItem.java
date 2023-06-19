package com.vinplay.item;

import java.io.Serializable;

public class SGUserItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sgid;
	private String loginname;
	private Integer sgcountid;
	public String getSgid() {
		return sgid;
	}
	public void setSgid(String sgid) {
		this.sgid = sgid;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Integer getSgcountid() {
		return sgcountid;
	}
	public void setSgcountid(Integer sgcountid) {
		this.sgcountid = sgcountid;
	}
	
}
