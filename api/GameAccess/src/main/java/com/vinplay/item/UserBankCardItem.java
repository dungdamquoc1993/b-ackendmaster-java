package com.vinplay.item;

public class UserBankCardItem implements java.io.Serializable{
	private static final long serialVersionUID = 7918506454460213995L;
	private Integer id;
	private String loginname;
	private String bankname;
	private String branchbank;
	private String name;
	private String bankcard;
	private String savetime;
	private Integer status;
	private String branchname;
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBankcard() {
		return bankcard;
	}
	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}
	public String getSavetime() {
		return savetime;
	}
	public void setSavetime(String savetime) {
		this.savetime = savetime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getBranchname() {
		return branchname;
	}
	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBranchbank() {
		return branchbank;
	}
	public void setBranchbank(String branchbank) {
		this.branchbank = branchbank;
	}
}
