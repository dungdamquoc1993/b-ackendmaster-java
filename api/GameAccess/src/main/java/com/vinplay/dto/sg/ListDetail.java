package com.vinplay.dto.sg;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ListDetail implements Serializable{
	private Double betAmt;
	private Double winAmt;
	private Double playerPL;
	private Double betCount;
	private String userId;
	private String currency;
	public Double getBetAmt() {
		return betAmt;
	}
	public void setBetAmt(Double betAmt) {
		this.betAmt = betAmt;
	}
	public Double getWinAmt() {
		return winAmt;
	}
	public void setWinAmt(Double winAmt) {
		this.winAmt = winAmt;
	}
	public Double getPlayerPL() {
		return playerPL;
	}
	public void setPlayerPL(Double playerPL) {
		this.playerPL = playerPL;
	}
	public Double getBetCount() {
		return betCount;
	}
	public void setBetCount(Double betCount) {
		this.betCount = betCount;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
}
