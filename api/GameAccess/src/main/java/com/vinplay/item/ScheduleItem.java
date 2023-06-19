package com.vinplay.item;

public class ScheduleItem implements java.io.Serializable{
	private static final long serialVersionUID = -4825927140135085283L;
	private int lotteryid;
	private String issue;
	private String opentime;
	private String sysopentime;
	private String endtime;
	private String starttime;
	private int status;
	private String winnumber;
	private int sale;
	private int left;
	
	private String basetime;
	private int openstep;
	private String remark;//备注
	public int getLotteryid() {
		return lotteryid;
	}
	public void setLotteryid(int lotteryid) {
		this.lotteryid = lotteryid;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getSysopentime() {
		return sysopentime;
	}
	public void setSysopentime(String sysopentime) {
		this.sysopentime = sysopentime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getWinnumber() {
		return winnumber;
	}
	public void setWinnumber(String winnumber) {
		this.winnumber = winnumber;
	}
	public int getSale() {
		return sale;
	}
	public void setSale(int sale) {
		this.sale = sale;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public String getOpentime() {
		return opentime;
	}
	public void setOpentime(String opentime) {
		this.opentime = opentime;
	}
	public String getBasetime() {
		return basetime;
	}
	public void setBasetime(String basetime) {
		this.basetime = basetime;
	}
	public int getOpenstep() {
		return openstep;
	}
	public void setOpenstep(int openstep) {
		this.openstep = openstep;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
