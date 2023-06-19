package com.vinplay.dto.ibc;

import java.io.Serializable;

public class CheckUserBalanceDetailsRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 7872658836135549090L;
	private String playerName;
	private Double balance;
	private Double outstanding;
	private Integer currency;
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getOutstanding() {
		return outstanding;
	}
	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

}
