package com.vinplay.item;

public class BalanceChangeItem implements java.io.Serializable{
	private static final long serialVersionUID = 6025888509389138740L;
	private String id;
	private String loginname=null;
	private String opttime;
	private Integer opttype;
	private String opttypename=null;
	
	private Integer lotteryid;
	private Integer methodid;
	private String issue;
	private String betmodle;
	private Double income;
	private Double pay;
	private String beforebalance;
	private String afterbalance;
	private String remark;
	private String insertby;
	private String inserttime;
	private String wid;
	private Integer fatherid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOpttime() {
		return opttime;
	}
	public void setOpttime(String opttime) {
		this.opttime = opttime;
	}
	public Integer getOpttype() {
		return opttype;
	}
	public void setOpttype(Integer opttype) {
		this.opttype = opttype;
	}
	public Integer getLotteryid() {
		return lotteryid;
	}
	public void setLotteryid(Integer lotteryid) {
		this.lotteryid = lotteryid;
	}
	public Integer getMethodid() {
		return methodid;
	}
	public void setMethodid(Integer methodid) {
		this.methodid = methodid;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public Double getIncome() {
		return income;
	}
	public void setIncome(Double income) {
		this.income = income;
	}
	public Double getPay() {
		return pay;
	}
	public void setPay(Double pay) {
		this.pay = pay;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getInsertby() {
		return insertby;
	}
	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}
	public String getInserttime() {
		return inserttime;
	}
	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public Integer getFatherid() {
		return fatherid;
	}
	public void setFatherid(Integer fatherid) {
		this.fatherid = fatherid;
	}
	public String getOpttypename() {
		return opttypename;
	}
	public void setOpttypename(String opttypename) {
		this.opttypename = opttypename;
	}
	public String getBetmodle() {
		return betmodle;
	}
	public void setBetmodle(String betmodle) {
		this.betmodle = betmodle;
	}
	public String getBeforebalance() {
		return beforebalance;
	}
	public void setBeforebalance(String beforebalance) {
		this.beforebalance = beforebalance;
	}
	public String getAfterbalance() {
		return afterbalance;
	}
	public void setAfterbalance(String afterbalance) {
		this.afterbalance = afterbalance;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
}
