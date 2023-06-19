package com.vinplay.item;

public class FHItem implements java.io.Serializable{
	private static final long serialVersionUID = -7974249671327841520L;
	private String loginname;
	private Integer userid;
	private Integer fatherid;
	private String sdate;
	private Double sale;
	private Double winloss;
	private Double point;
	private Double amount;
	private String fatherstr;
	private Integer status;
	private String msg;
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Integer getFatherid() {
		return fatherid;
	}
	public void setFatherid(Integer fatherid) {
		this.fatherid = fatherid;
	}
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public Double getSale() {
		return sale;
	}
	public void setSale(Double sale) {
		this.sale = sale;
	}
	public Double getWinloss() {
		return winloss;
	}
	public void setWinloss(Double winloss) {
		this.winloss = winloss;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getFatherstr() {
		return fatherstr;
	}
	public void setFatherstr(String fatherstr) {
		this.fatherstr = fatherstr;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Double getPoint() {
		return point;
	}
	public void setPoint(Double point) {
		this.point = point;
	}
}
