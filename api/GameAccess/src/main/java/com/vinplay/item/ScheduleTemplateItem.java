package com.vinplay.item;

public class ScheduleTemplateItem implements java.io.Serializable{


	private static final long serialVersionUID = 3214228709664224495L;
	private Integer id;
	private Integer lotteryid;
	private String opentime;
	private String sysopentime;
	private String endtime;
	private String starttime;
	private Integer sale;
	private Integer left;
	private String basetime;
	
	private String issue;
	
	
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLotteryid() {
		return lotteryid;
	}
	public void setLotteryid(Integer lotteryid) {
		this.lotteryid = lotteryid;
	}
	public String getOpentime() {
		return opentime;
	}
	public void setOpentime(String opentime) {
		this.opentime = opentime;
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
	public Integer getSale() {
		return sale;
	}
	public void setSale(Integer sale) {
		this.sale = sale;
	}
	public Integer getLeft() {
		return left;
	}
	public void setLeft(Integer left) {
		this.left = left;
	}
	public String getBasetime() {
		return basetime;
	}
	public void setBasetime(String basetime) {
		this.basetime = basetime;
	}
	

}
