package com.vinplay.dto.sg;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GameInfoDetailResp implements Serializable{
	private String roundStartTime;
	private String winner;
	private String ip;
	private String dealerDomain;
	private String status;
	private Double odds;
	private Double tableId;
	private Double winLoss;
	public String getRoundStartTime() {
		return roundStartTime;
	}
	public void setRoundStartTime(String roundStartTime) {
		this.roundStartTime = roundStartTime;
	}
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDealerDomain() {
		return dealerDomain;
	}
	public void setDealerDomain(String dealerDomain) {
		this.dealerDomain = dealerDomain;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Double getOdds() {
		return odds;
	}
	public void setOdds(Double odds) {
		this.odds = odds;
	}
	public Double getTableId() {
		return tableId;
	}
	public void setTableId(Double tableId) {
		this.tableId = tableId;
	}
	public Double getWinLoss() {
		return winLoss;
	}
	public void setWinLoss(Double winLoss) {
		this.winLoss = winLoss;
	}
	

}
