package com.vinplay.item;

public class LockBetItem implements java.io.Serializable{
	private static final long serialVersionUID = 1414340171672206851L;
	private int lotteryid;
	private int methodid;
	private String issue;
	private Double maxbet=0.0;
	private Double curbet=0.0;
	
	private String methodname;
	private String lotteryname;
	private String type;
	private String allmethods;
	private String checkflag;
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
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public Double getMaxbet() {
		return maxbet;
	}
	public void setMaxbet(Double maxbet) {
		this.maxbet = maxbet;
	}
	public Double getCurbet() {
		return curbet;
	}
	public void setCurbet(Double curbet) {
		this.curbet = curbet;
	}
	public String getMethodname() {
		return methodname;
	}
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	public String getLotteryname() {
		return lotteryname;
	}
	public void setLotteryname(String lotteryname) {
		this.lotteryname = lotteryname;
	}
	
	public String getType(){
		return this.type;
	}
	
	public void setType(String type){
		this.type=type;
	}
	
	public String getAllmethods(){
		return this.allmethods;
	}
	
	public void setAllmethods(String allmethods){
		this.allmethods=allmethods;
	}
	
	public String getCheckflag(){
		return this.checkflag;
	}
	
	public void setCheckflag(String checkflag){
		this.checkflag=checkflag;
	}
}
