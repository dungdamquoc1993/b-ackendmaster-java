package com.vinplay.dto.sa;

import java.io.Serializable;

public class SABetDetails implements Serializable {

	private static final long serialVersionUID = 5320617391338614647L;
	/**
	 * 
	 */
	private String betTime;
	private String payoutTime;
	private String userName;
	private int hostID;
	private String detail;
	private String gameID;
	private int round;
	private int set;
	private long betID;
	private double betAmount;
	private double rolling;
	private double resultAmount;
	private double balance;
	private String gameType;
	private int betType;
	private int betSource;
	private long transactionID;
	private String state;
	private String gameResult;
	
	public SABetDetails() {
	}

	public SABetDetails(String betTime, String payoutTime, String userName, String hostID, String gameID,
			String round, String set, String betID, String betAmount, String rolling, String resultAmount,
			String balance, String gameType, String betType, String betSource, String transactionID, String state,
			String gameResult, int rate) {
		this.betTime = betTime;
		this.payoutTime = payoutTime;
		this.userName = userName;
		this.hostID = Integer.parseInt(hostID);
		this.gameID = gameID;
		this.round = Integer.parseInt(round);
		this.set = Integer.parseInt(set);
		this.betID = Long.parseLong(betID);
		this.betAmount = Double.parseDouble(betAmount) * rate;
		this.rolling = Double.parseDouble(rolling) * rate;
		this.resultAmount = Double.parseDouble(resultAmount) * rate;
		this.balance = Double.parseDouble(balance) * rate;
		this.gameType = gameType;
		this.betType = Integer.parseInt(betType);
		this.betSource = Integer.parseInt(betSource);
		this.transactionID = Long.parseLong(transactionID);
		this.state = state;
		this.gameResult = gameResult;
	}

	public String getGameResult() {
		return gameResult;
	}

	public void setGameResult(String gameResult) {
		this.gameResult = gameResult;
	}

	public String getBetTime() {
		return betTime;
	}

	public void setBetTime(String betTime) {
		this.betTime = betTime;
	}

	public String getPayoutTime() {
		return payoutTime;
	}

	public void setPayoutTime(String payoutTime) {
		this.payoutTime = payoutTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getHostID() {
		return hostID;
	}

	public void setHostID(int hostID) {
		this.hostID = hostID;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getGameID() {
		return gameID;
	}

	public void setGameID(String gameID) {
		this.gameID = gameID;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getSet() {
		return set;
	}

	public void setSet(int set) {
		this.set = set;
	}

	public long getBetID() {
		return betID;
	}

	public void setBetID(long betID) {
		this.betID = betID;
	}

	public double getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(double betAmount) {
		this.betAmount = betAmount;
	}

	public double getRolling() {
		return rolling;
	}

	public void setRolling(double rolling) {
		this.rolling = rolling;
	}

	public double getResultAmount() {
		return resultAmount;
	}

	public void setResultAmount(double resultAmount) {
		this.resultAmount = resultAmount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public int getBetType() {
		return betType;
	}

	public void setBetType(int betType) {
		this.betType = betType;
	}

	public int getBetSource() {
		return betSource;
	}

	public void setBetSource(int betSource) {
		this.betSource = betSource;
	}

	public long getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(long transactionID) {
		this.transactionID = transactionID;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
