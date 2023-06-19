package com.vinplay.dto.ebet;

import java.io.Serializable;

public class ResultsDto implements Serializable{
	private static final long serialVersionUID = -4218533889892341262L;
	private String username;
	private Double money;
	private WalletDto[] wallet;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public WalletDto[] getWallet() {
		return wallet;
	}
	public void setWallet(WalletDto[] wallet) {
		this.wallet = wallet;
	}


}
