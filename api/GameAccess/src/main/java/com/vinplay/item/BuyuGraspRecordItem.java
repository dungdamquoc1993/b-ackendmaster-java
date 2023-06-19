package com.vinplay.item;

import java.io.Serializable;

public class BuyuGraspRecordItem implements Serializable{

	private static final long serialVersionUID = -5768167823689201464L;


	private String id;
	
	
	private String loginname;
	
	
	private String inserttime;
	
	
	private Double amount;
	
	
	private Double bonus;


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


	public String getInserttime() {
		return inserttime;
	}


	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public Double getBonus() {
		return bonus;
	}


	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}
}
