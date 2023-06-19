package com.vinplay.item;

import java.io.Serializable;

public class GameAccessUserItem implements Serializable{

	private static final long serialVersionUID = 3139503159543569344L;

	private Long id;
	
	private String loginname;
	
	private String becuserid;
	
	private Integer becflag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getBecuserid() {
		return becuserid;
	}

	public void setBecuserid(String becuserid) {
		this.becuserid = becuserid;
	}

	public Integer getBecflag() {
		return becflag;
	}

	public void setBecflag(Integer becflag) {
		this.becflag = becflag;
	}

}
