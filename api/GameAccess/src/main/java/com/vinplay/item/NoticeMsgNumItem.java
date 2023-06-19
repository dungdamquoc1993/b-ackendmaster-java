package com.vinplay.item;

import java.io.Serializable;

public class NoticeMsgNumItem implements Serializable{

	private static final long serialVersionUID = -4535436627981202963L;

	private Integer passOpenNum =0;
	
	private Integer dpNum =0;
	
	private Integer dpedNum =0;
	
	private Integer wdNum =0;
	
	private Integer wdedNum =0;
	
	private Integer bgNum =0;
	
	private Integer tipNum = 0;

	private Integer buyuNum = 0;
	
	
	
	public Integer getDpedNum() {
		return dpedNum;
	}

	public void setDpedNum(Integer dpedNum) {
		this.dpedNum = dpedNum;
	}

	public Integer getWdedNum() {
		return wdedNum;
	}

	public void setWdedNum(Integer wdedNum) {
		this.wdedNum = wdedNum;
	}

	public Integer getBuyuNum() {
		return buyuNum;
	}

	public void setBuyuNum(Integer buyuNum) {
		this.buyuNum = buyuNum;
	}

	public Integer getPassOpenNum() {
		return passOpenNum;
	}

	public void setPassOpenNum(Integer passOpenNum) {
		this.passOpenNum = passOpenNum;
	}

	public Integer getDpNum() {
		return dpNum;
	}

	public void setDpNum(Integer dpNum) {
		this.dpNum = dpNum;
	}

	public Integer getWdNum() {
		return wdNum;
	}

	public void setWdNum(Integer wdNum) {
		this.wdNum = wdNum;
	}

	public Integer getBgNum() {
		return bgNum;
	}

	public void setBgNum(Integer bgNum) {
		this.bgNum = bgNum;
	}

	public Integer getTipNum() {
		return tipNum;
	}

	public void setTipNum(Integer tipNum) {
		this.tipNum = tipNum;
	}
}
