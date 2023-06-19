package com.vinplay.item;

import java.io.Serializable;

public class BettingItem implements Serializable {
	private static final long serialVersionUID = 5948964203077475856L;
	private String type;
	private int lotteryid;
	private String issue;
	private Integer methodid=-1;
	private String codes;
	private Integer nums=0;
	private Integer times=0;
	private double money=0;
	private Integer mode=0;
	private double point;
	private String desc;
	private long curtimes;
	private String position;
	private Integer zip=0;
	private double odd;
	private double rebate;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public Integer getMethodid() {
		return methodid;
	}

	public void setMethodid(Integer methodid) {
		this.methodid = methodid;
	}

	public String getCodes() {
		return codes;
	}

	public void setCodes(String codes) {
		this.codes = codes;
	}

	public Integer getNums() {
		return nums;
	}

	public void setNums(Integer nums) {
		this.nums = nums;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public double getPoint() {
		return point;
	}

	public void setPoint(double point) {
		this.point = point;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getCurtimes() {
		return curtimes;
	}

	public void setCurtimes(long curtimes) {
		this.curtimes = curtimes;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Integer getZip() {
		return zip;
	}

	public void setZip(Integer zip) {
		this.zip = zip;
	}

	public double getOdd() {
		return odd;
	}

	public void setOdd(double odd) {
		this.odd = odd;
	}

	public double getRebate() {
		return rebate;
	}

	public void setRebate(double rebate) {
		this.rebate = rebate;
	}
}
