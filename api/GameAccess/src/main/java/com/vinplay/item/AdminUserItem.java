package com.vinplay.item;

public class AdminUserItem implements java.io.Serializable{
	private static final long serialVersionUID = 1670505206135654945L;
	private String loginname=null;
	private String password;
	private String lastlogintime;
	private String lastloginip;
	private Integer logintimes;
	private String roles;
	private Integer fatherid;
	private String nickname;
	private String fullname;
	private Integer whriteuser;
	
	/**
	 * 用户状态
	 * add by donny
	 */
	private Integer status;
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public Integer getFatherid() {
		return fatherid;
	}
	public void setFatherid(Integer fatherid) {
		this.fatherid = fatherid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getLastlogintime() {
		return lastlogintime;
	}
	public void setLastlogintime(String lastlogintime) {
		this.lastlogintime = lastlogintime;
	}
	public String getLastloginip() {
		return lastloginip;
	}
	public void setLastloginip(String lastloginip) {
		this.lastloginip = lastloginip;
	}
	public Integer getLogintimes() {
		return logintimes;
	}
	public void setLogintimes(Integer logintimes) {
		this.logintimes = logintimes;
	}
	public Integer getWhriteuser() {
		return whriteuser;
	}
	public void setWhriteuser(Integer whriteuser) {
		this.whriteuser = whriteuser;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
