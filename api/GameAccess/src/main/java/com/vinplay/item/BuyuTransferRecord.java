package com.vinplay.item;

import java.io.Serializable;

public class BuyuTransferRecord implements Serializable{

	private static final long serialVersionUID = 1477883759920397634L;

	private String id;
	
	private String userid;
	
	private Integer transfertype;
	
	private String transfertime;
	
	private String amountarrivetime;
	
	private Double amount;
	
	private Double buyuwallet;
	
	private Integer status;
	
	private String remark;
	
	private String admin1;
	
	private String admin2;

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

	public Integer getTransfertype() {
		return transfertype;
	}

	public void setTransfertype(Integer transfertype) {
		this.transfertype = transfertype;
	}

	public String getTransfertime() {
		return transfertime;
	}

	public void setTransfertime(String transfertime) {
		this.transfertime = transfertime;
	}

	public String getAmountarrivetime() {
		return amountarrivetime;
	}

	public void setAmountarrivetime(String amountarrivetime) {
		this.amountarrivetime = amountarrivetime;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getBuyuwallet() {
		return buyuwallet;
	}

	public void setBuyuwallet(Double buyuwallet) {
		this.buyuwallet = buyuwallet;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAdmin1() {
		return admin1;
	}

	public void setAdmin1(String admin1) {
		this.admin1 = admin1;
	}

	public String getAdmin2() {
		return admin2;
	}

	public void setAdmin2(String admin2) {
		this.admin2 = admin2;
	}
}
