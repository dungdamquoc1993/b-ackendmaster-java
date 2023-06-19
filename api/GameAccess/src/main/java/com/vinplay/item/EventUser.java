package com.vinplay.item;

public class EventUser implements java.io.Serializable{
	private static final long serialVersionUID = 390575679782084017L;
	private String loginname;
	private Integer eventid;
	private Integer rank;
	private Integer presentcount;
	private String createtime;
	private String updatetime;
	private String admin;
	private Double amount;
	private String remark;
	private String payouttime;
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Integer getEventid() {
		return eventid;
	}
	public void setEventid(Integer eventid) {
		this.eventid = eventid;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Integer getPresentcount() {
		return presentcount;
	}
	public void setPresentcount(Integer presentcount) {
		this.presentcount = presentcount;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public Double getAmount() {
		return amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPayouttime() {
		return payouttime;
	}
	public void setPayouttime(String payouttime) {
		this.payouttime = payouttime;
	}
}
