package com.vinplay.dto.ebet;

import java.io.Serializable;

public class BetMapDto implements Serializable{
	private static final long serialVersionUID = -7922008093981204827L;
	private Integer betType;
	private Integer[] betNumber;
	private Double betMoney;
	private String betTypeInterval;
	private String betHistoryId;
	
	public Integer getBetType() {
		return betType;
	}
	public void setBetType(Integer betType) {
		this.betType = betType;
	}
	public Integer[] getBetNumber() {
		return betNumber;
	}
	public void setBetNumber(Integer[] betNumber) {
		this.betNumber = betNumber;
	}
	public Double getBetMoney() {
		return betMoney;
	}
	public void setBetMoney(Double betMoney) {
		this.betMoney = betMoney;
	}
	public String getBetTypeInterval() {
		return betTypeInterval;
	}
	public void setBetTypeInterval(String betTypeInterval) {
		this.betTypeInterval = betTypeInterval;
	}
	public String getBetHistoryId() {
		return betHistoryId;
	}
	public void setBetHistoryId(String betHistoryId) {
		this.betHistoryId = betHistoryId;
	}
	
}
