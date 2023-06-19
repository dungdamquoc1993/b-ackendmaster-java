package com.vinplay.dto.wm;

@SuppressWarnings("serial")
public class LogInReqDto extends BaseReqDto {
	private String user;
	private String password;
	private Integer lang;
	private String mode;
	private String returnurl;
	private Integer isTest;
	private Integer size;
	private Integer ui;
	private Integer site;
	private Integer syslang;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getReturnurl() {
		return returnurl;
	}

	public void setReturnurl(String returnurl) {
		this.returnurl = returnurl;
	}

	public Integer getIsTest() {
		return isTest;
	}

	public void setIsTest(Integer isTest) {
		this.isTest = isTest;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getUi() {
		return ui;
	}

	public void setUi(Integer ui) {
		this.ui = ui;
	}

	public Integer getSite() {
		return site;
	}

	public void setSite(Integer site) {
		this.site = site;
	}

	public Integer getSyslang() {
		return syslang;
	}

	public void setSyslang(Integer syslang) {
		this.syslang = syslang;
	}

}
