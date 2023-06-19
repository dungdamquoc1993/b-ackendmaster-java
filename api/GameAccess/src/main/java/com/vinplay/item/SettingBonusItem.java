package com.vinplay.item;

import java.io.Serializable;

public class SettingBonusItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4534215071754682568L;
	private Double bonus;
	private String type;
	private String remark;

	private String starttime;
	private String endtime;

	private String starttimeslot;
	private String endtimeslot;

	private Double minmoney;
	private Double maxmoney;

	private String method;

	private Integer status;
	
	private String statusRealtime;
	
	private String value;

	public Double getBonus() {
		return bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getStarttimeslot() {
		return starttimeslot;
	}

	public void setStarttimeslot(String starttimeslot) {
		this.starttimeslot = starttimeslot;
	}

	public String getEndtimeslot() {
		return endtimeslot;
	}

	public void setEndtimeslot(String endtimeslot) {
		this.endtimeslot = endtimeslot;
	}

	public Double getMinmoney() {
		return minmoney;
	}

	public void setMinmoney(Double minmoney) {
		this.minmoney = minmoney;
	}

	public Double getMaxmoney() {
		return maxmoney;
	}

	public void setMaxmoney(Double maxmoney) {
		this.maxmoney = maxmoney;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusRealtime() {
		return statusRealtime;
	}

	public void setStatusRealtime(String statusRealtime) {
		this.statusRealtime = statusRealtime;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	

}
