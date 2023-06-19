package com.vinplay.item;

public class WithdrawalItem implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private Integer fatherid;
	private String loginname;
	private Double amount;
	private String applytime;
	private String fatherflag;
	private Integer status;
	private String remark;
	private Integer cardid;
	private Integer hurryuptimes;
	
	private String adminuser1=null;
	private String adminuser2=null;
	private int monthlevelvip=0;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getFatherid() {
		return fatherid;
	}
	public void setFatherid(Integer fatherid) {
		this.fatherid = fatherid;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getApplytime() {
		return applytime;
	}
	public void setApplytime(String applytime) {
		this.applytime = applytime;
	}
	public String getFatherflag() {
		return fatherflag;
	}
	public void setFatherflag(String fatherflag) {
		this.fatherflag = fatherflag;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getCardid() {
		return cardid;
	}
	public void setCardid(Integer cardid) {
		this.cardid = cardid;
	}
	public Integer getHurryuptimes() {
		return hurryuptimes;
	}
	public void setHurryuptimes(Integer hurryuptimes) {
		this.hurryuptimes = hurryuptimes;
	}
	public String getAdminuser1() {
		return adminuser1;
	}
	public void setAdminuser1(String adminuser1) {
		this.adminuser1 = adminuser1;
	}
	public String getAdminuser2() {
		return adminuser2;
	}
	public void setAdminuser2(String adminuser2) {
		this.adminuser2 = adminuser2;
	}
	public int getMonthlevelvip() {
		return monthlevelvip;
	}
	public void setMonthlevelvip(int monthlevelvip) {
		this.monthlevelvip = monthlevelvip;
	}
	
}
