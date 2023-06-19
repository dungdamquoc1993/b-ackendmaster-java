package com.vinplay.dto.sg;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TransactionsDetailRespDto implements Serializable{
	private String gameType;
	private String txTime;
	private String updateTime;
	private String userId;
	private String betType;
	private String platform;
	private String platformTxId;
	private String gameCode;
	private String currency;
	private String roundId;
	private Double winAmount;
	private Double betAmount;
	private Double jackpotWinAmount;
	private Double turnover;
	private Integer txStatus;
	private Double jackpotBetAmount;
	private Double realBetAmount;
	private Double realWinAmount ;
	private Long ID;
	private String gameInfo;
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public String getTxTime() {
		return txTime;
	}
	public void setTxTime(String txTime) {
		this.txTime = txTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBetType() {
		return betType;
	}
	public void setBetType(String betType) {
		this.betType = betType;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getPlatformTxId() {
		return platformTxId;
	}
	public void setPlatformTxId(String platformTxId) {
		this.platformTxId = platformTxId;
	}
	public String getGameCode() {
		return gameCode;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getRoundId() {
		return roundId;
	}
	public void setRoundId(String roundId) {
		this.roundId = roundId;
	}
	public Double getWinAmount() {
		return winAmount;
	}
	public void setWinAmount(Double winAmount) {
		this.winAmount = winAmount;
	}
	public Double getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}
	public Double getJackpotWinAmount() {
		return jackpotWinAmount;
	}
	public void setJackpotWinAmount(Double jackpotWinAmount) {
		this.jackpotWinAmount = jackpotWinAmount;
	}
	public Double getTurnover() {
		return turnover;
	}
	public void setTurnover(Double turnover) {
		this.turnover = turnover;
	}
	public Integer getTxStatus() {
		return txStatus;
	}
	public void setTxStatus(Integer txStatus) {
		this.txStatus = txStatus;
	}
	public Double getJackpotBetAmount() {
		return jackpotBetAmount;
	}
	public void setJackpotBetAmount(Double jackpotBetAmount) {
		this.jackpotBetAmount = jackpotBetAmount;
	}
	public Double getRealBetAmount() {
		return realBetAmount;
	}
	public void setRealBetAmount(Double realBetAmount) {
		this.realBetAmount = realBetAmount;
	}
	public Double getRealWinAmount() {
		return realWinAmount;
	}
	public void setRealWinAmount(Double realWinAmount) {
		this.realWinAmount = realWinAmount;
	}
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public String getGameInfo() {
		return gameInfo;
	}
	public void setGameInfo(String gameInfo) {
		this.gameInfo = gameInfo;
	}
	
}
