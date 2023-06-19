package com.vinplay.dto.sa;

import java.io.Serializable;

public class SAUserInfoResp extends BaseResponseDto implements Serializable{

	private static final long serialVersionUID = -579924025463733340L;
	/**
	 * 
	 */
	private String said;
	private String isSuccess;
	private String userName;
	private double balance;
	private String online;
	private String betted;
	private double bettedAmount;
	private double maxBalance;
	private double maxWinning;
	private double withholdAmount;
	
	
	public String getSaid() {
		return said;
	}
	public void setSaid(String said) {
		this.said = said;
	}
	public String getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getOnline() {
		return online;
	}
	public void setOnline(String online) {
		this.online = online;
	}
	public String getBetted() {
		return betted;
	}
	public void setBetted(String betted) {
		this.betted = betted;
	}
	public double getBettedAmount() {
		return bettedAmount;
	}
	public void setBettedAmount(double bettedAmount) {
		this.bettedAmount = bettedAmount;
	}
	public double getMaxBalance() {
		return maxBalance;
	}
	public void setMaxBalance(double maxBalance) {
		this.maxBalance = maxBalance;
	}
	public double getMaxWinning() {
		return maxWinning;
	}
	public void setMaxWinning(double maxWinning) {
		this.maxWinning = maxWinning;
	}
	public double getWithholdAmount() {
		return withholdAmount;
	}
	public void setWithholdAmount(double withholdAmount) {
		this.withholdAmount = withholdAmount;
	}
	
	
	
}
