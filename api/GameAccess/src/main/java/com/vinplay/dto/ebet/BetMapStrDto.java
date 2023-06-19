package com.vinplay.dto.ebet;

import java.io.Serializable;

public class BetMapStrDto implements Serializable{
	private static final long serialVersionUID = -7931048071251595360L;
	private Integer bettype;
	private String betnumber;
	private Double betmoney;
	private String bettypeinterval;
	private String bethistoryid;
	public Integer getBettype() {
		return bettype;
	}
	public void setBettype(Integer bettype) {
		this.bettype = bettype;
	}
	public String getBetnumber() {
		return betnumber;
	}
	public void setBetnumber(String betnumber) {
		this.betnumber = betnumber;
	}
	public Double getBetmoney() {
		return betmoney;
	}
	public void setBetmoney(Double betmoney) {
		this.betmoney = betmoney;
	}
	public String getBettypeinterval() {
		return bettypeinterval;
	}
	public void setBettypeinterval(String bettypeinterval) {
		this.bettypeinterval = bettypeinterval;
	}
	public String getBethistoryid() {
		return bethistoryid;
	}
	public void setBethistoryid(String bethistoryid) {
		this.bethistoryid = bethistoryid;
	}
	
}
