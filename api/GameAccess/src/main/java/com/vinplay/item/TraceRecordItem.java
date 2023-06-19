package com.vinplay.item;

public class TraceRecordItem implements java.io.Serializable{
	private static final long serialVersionUID = -961128067099164042L;
	private String id;
	private String loginname;
	private String bettime;
	private int lotteryid;
	private int methodid;
	private String issuestart;
	private int finishissue;
	private int tracenumber;
	private String tracecontent;
	private int betmodle;
	private double amount;
	private double finishamount;
	private int status;
	private String inserttime;
	private String updatetime;
	private String insertby;
	private int fatherflag;
	private int cancelissue;
	private double cancelamount;
	private String winstop;
	private String winnumber;
	
	private String lotteryname=null;
	private String methodname=null;
	private String statusname=null;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getBettime() {
		return bettime;
	}
	public void setBettime(String bettime) {
		this.bettime = bettime;
	}
	public int getLotteryid() {
		return lotteryid;
	}
	public void setLotteryid(int lotteryid) {
		this.lotteryid = lotteryid;
	}
	public int getMethodid() {
		return methodid;
	}
	public void setMethodid(int methodid) {
		this.methodid = methodid;
	}
	public String getIssuestart() {
		return issuestart;
	}
	public void setIssuestart(String issuestart) {
		this.issuestart = issuestart;
	}
	public int getFinishissue() {
		return finishissue;
	}
	public void setFinishissue(int finishissue) {
		this.finishissue = finishissue;
	}
	public int getTracenumber() {
		return tracenumber;
	}
	public void setTracenumber(int tracenumber) {
		this.tracenumber = tracenumber;
	}
	public String getTracecontent() {
		return tracecontent;
	}
	public void setTracecontent(String tracecontent) {
		this.tracecontent = tracecontent;
	}
	public int getBetmodle() {
		return betmodle;
	}
	public void setBetmodle(int betmodle) {
		this.betmodle = betmodle;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getFinishamount() {
		return finishamount;
	}
	public void setFinishamount(double finishamount) {
		this.finishamount = finishamount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getInsertby() {
		return insertby;
	}
	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}
	public int getFatherflag() {
		return fatherflag;
	}
	public void setFatherflag(int fatherflag) {
		this.fatherflag = fatherflag;
	}
	public int getCancelissue() {
		return cancelissue;
	}
	public void setCancelissue(int cancelissue) {
		this.cancelissue = cancelissue;
	}
	public double getCancelamount() {
		return cancelamount;
	}
	public void setCancelamount(double cancelamount) {
		this.cancelamount = cancelamount;
	}
	public String getWinstop() {
		return winstop;
	}
	public void setWinstop(String winstop) {
		this.winstop = winstop;
	}
	public String getWinnumber() {
		return winnumber;
	}
	public void setWinnumber(String winnumber) {
		this.winnumber = winnumber;
	}
	public String getLotteryname() {
		return lotteryname;
	}
	public void setLotteryname(String lotteryname) {
		this.lotteryname = lotteryname;
	}
	public String getMethodname() {
		return methodname;
	}
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	public String getStatusname() {
		return statusname;
	}
	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}
	public String getInserttime() {
		return inserttime;
	}
	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}
}
