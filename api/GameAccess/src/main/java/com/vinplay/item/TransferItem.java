package com.vinplay.item;

public class TransferItem implements java.io.Serializable{
	private static final long serialVersionUID = 353769105213182451L;
	private String id;
	private String userid;
	private Integer fatherid;
	private Double amount;
	private Integer transfertype;
	private Integer status;
	private String transfertime;
	private String wid;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Integer getFatherid() {
		return fatherid;
	}
	public void setFatherid(Integer fatherid) {
		this.fatherid = fatherid;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getTransfertype() {
		return transfertype;
	}
	public void setTransfertype(Integer transfertype) {
		this.transfertype = transfertype;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTransfertime() {
		return transfertime;
	}
	public void setTransfertime(String transfertime) {
		this.transfertime = transfertime;
	}
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
}
