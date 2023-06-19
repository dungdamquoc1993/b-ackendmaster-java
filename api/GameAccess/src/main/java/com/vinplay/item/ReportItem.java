package com.vinplay.item;

public class ReportItem implements java.io.Serializable{
	private static final long serialVersionUID = 897450488631175010L;
	private String reporttime;
	private Double deposit;
	private Double withdrawal;
	private Double betamount;
	private Double playerwin;
	private Double playerloss;
	private Double income;
	private Integer fatherid=-1;
	
	public String getReporttime() {
		return reporttime;
	}
	public void setReporttime(String reporttime) {
		this.reporttime = reporttime;
	}
	public Double getDeposit() {
		return deposit;
	}
	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}
	public Double getWithdrawal() {
		return withdrawal;
	}
	public void setWithdrawal(Double withdrawal) {
		this.withdrawal = withdrawal;
	}
	public Double getBetamount() {
		return betamount;
	}
	public void setBetamount(Double betamount) {
		this.betamount = betamount;
	}
	public Double getPlayerwin() {
		return playerwin;
	}
	public void setPlayerwin(Double playerwin) {
		this.playerwin = playerwin;
	}
	public Double getPlayerloss() {
		return playerloss;
	}
	public void setPlayerloss(Double playerloss) {
		this.playerloss = playerloss;
	}
	public Double getIncome() {
		return income;
	}
	public void setIncome(Double income) {
		this.income = income;
	}
	public Integer getFatherid() {
		return fatherid;
	}
	public void setFatherid(Integer fatherid) {
		this.fatherid = fatherid;
	}
}
