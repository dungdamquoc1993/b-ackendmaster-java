package com.vinplay.dto.ebet;

import java.io.Serializable;
import java.util.List;

public class BetHistoriesDto implements Serializable{
	private static final long serialVersionUID = -3588205473236794005L;
	private Integer gameType;
	private List<BetMapDto> betMap;
	private Integer[] judgeResult;
	private String roundNo;
	private Double bet;
	private Double payout;
	private Double balance;
	private Integer[] bankerCards;
	private Integer[] playerCards;
	private Integer[] allDices;
	private Integer dragonCard;
	private Integer tigerCard;
	private Integer number;
	private Integer createTime;
	private Integer payoutTime;
	private String betHistoryId;
	private Double validBet;
	private String username;
	private Integer userId;
	private String gameName;
	private Integer platform;
	
	public Integer getGameType() {
		return gameType;
	}
	public void setGameType(Integer gameType) {
		this.gameType = gameType;
	}
	public List<BetMapDto> getBetMap() {
		return betMap;
	}
	public void setBetMap(List<BetMapDto> betMap) {
		this.betMap = betMap;
	}
	public Integer[] getJudgeResult() {
		return judgeResult;
	}
	public void setJudgeResult(Integer[] judgeResult) {
		this.judgeResult = judgeResult;
	}
	public String getRoundNo() {
		return roundNo;
	}
	public void setRoundNo(String roundNo) {
		this.roundNo = roundNo;
	}
	public Double getBet() {
		return bet;
	}
	public void setBet(Double bet) {
		this.bet = bet;
	}
	public Double getPayout() {
		return payout;
	}
	public void setPayout(Double payout) {
		this.payout = payout;
	}
	public Integer[] getBankerCards() {
		return bankerCards;
	}
	public void setBankerCards(Integer[] bankerCards) {
		this.bankerCards = bankerCards;
	}
	public Integer[] getPlayerCards() {
		return playerCards;
	}
	public void setPlayerCards(Integer[] playerCards) {
		this.playerCards = playerCards;
	}
	public Integer[] getAllDices() {
		return allDices;
	}
	public void setAllDices(Integer[] allDices) {
		this.allDices = allDices;
	}
	public Integer getDragonCard() {
		return dragonCard;
	}
	public void setDragonCard(Integer dragonCard) {
		this.dragonCard = dragonCard;
	}
	public Integer getTigerCard() {
		return tigerCard;
	}
	public void setTigerCard(Integer tigerCard) {
		this.tigerCard = tigerCard;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Integer getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	public Integer getPayoutTime() {
		return payoutTime;
	}
	public void setPayoutTime(Integer payoutTime) {
		this.payoutTime = payoutTime;
	}
	public String getBetHistoryId() {
		return betHistoryId;
	}
	public void setBetHistoryId(String betHistoryId) {
		this.betHistoryId = betHistoryId;
	}
	public Double getValidBet() {
		return validBet;
	}
	public void setValidBet(Double validBet) {
		this.validBet = validBet;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public Integer getPlatform() {
		return platform;
	}
	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
}
