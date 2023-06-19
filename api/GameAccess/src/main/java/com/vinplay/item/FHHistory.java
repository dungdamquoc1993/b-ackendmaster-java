package com.vinplay.item;

public class FHHistory implements java.io.Serializable{
	private static final long serialVersionUID = -6553821786417965618L;
	private String loginname;
	private Double amount;
	private Integer status;
	private String maketime;
	private String finishtime;
	private String adminuser;
	private String adminip;
	private Double reamount;
	private String rdate;
	private Double winloss;
	private Double point;
	private Integer fatherid;
	private String remark;
	private Integer userid;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMaketime() {
		return maketime;
	}
	public void setMaketime(String maketime) {
		this.maketime = maketime;
	}
	public String getFinishtime() {
		return finishtime;
	}
	public void setFinishtime(String finishtime) {
		this.finishtime = finishtime;
	}
	public String getAdminuser() {
		return adminuser;
	}
	public void setAdminuser(String adminuser) {
		this.adminuser = adminuser;
	}
	public String getAdminip() {
		return adminip;
	}
	public void setAdminip(String adminip) {
		this.adminip = adminip;
	}
	public Double getReamount() {
		return reamount;
	}
	public void setReamount(Double reamount) {
		this.reamount = reamount;
	}
	public String getRdate() {
		return rdate;
	}
	public void setRdate(String rdate) {
		this.rdate = rdate;
	}
	public Double getWinloss() {
		return winloss;
	}
	public void setWinloss(Double winloss) {
		this.winloss = winloss;
	}
	public Integer getFatherid() {
		return fatherid;
	}
	public void setFatherid(Integer fatherid) {
		this.fatherid = fatherid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Double getPoint() {
		return point;
	}
	public void setPoint(Double point) {
		this.point = point;
	}
}
