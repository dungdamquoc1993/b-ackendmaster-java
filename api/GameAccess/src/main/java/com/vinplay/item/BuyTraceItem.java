package com.vinplay.item;
/**
 * 购买时追号列�?
 * @author Administrator
 *
 */
public class BuyTraceItem implements java.io.Serializable{
	private static final long serialVersionUID = 6914351004003651898L;
	private String issue;
	private int times;
	
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
}
