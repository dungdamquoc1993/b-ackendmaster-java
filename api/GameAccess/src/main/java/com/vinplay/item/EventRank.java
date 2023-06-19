package com.vinplay.item;

public class EventRank implements java.io.Serializable{
	private static final long serialVersionUID = -7577467981535695443L;
	private Double totalrecharge;
	private Double totallose;
	private Double present;
	private Integer rank;
	private Integer eventid;
	private String remark;
	
	public Double getTotalrecharge() {
		return totalrecharge;
	}
	public void setTotalrecharge(Double totalrecharge) {
		this.totalrecharge = totalrecharge;
	}
	public Double getTotallose() {
		return totallose;
	}
	public void setTotallose(Double totallose) {
		this.totallose = totallose;
	}
	public Double getPresent() {
		return present;
	}
	public void setPresent(Double present) {
		this.present = present;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Integer getEventid() {
		return eventid;
	}
	public void setEventid(Integer eventid) {
		this.eventid = eventid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
