/**
 * Archie
 */
package com.vinplay.dto.sbo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Archie
 *
 */
import com.fasterxml.jackson.annotation.JsonProperty;

public class SboRecordDetail implements Serializable {

	private String refNo;// "string",
	private String username;// "string",
	private String sportsType;// "string",
	private String orderTime;// "DateTime(UTC-4)",
	private String winLostDate;// "DateTime(UTC-4)",
	private String modifyDate;// "DateTime(UTC-4)",
	private String settleTime;
	private Double odds;// "double",
	private String oddsStyle;// "string",
	private Double stake;// "double",
	private Double actualStake;// "double",
	private String currency;// "string",
	private String status;// "string",
	private Double winLost;// "double",
	private Double turnover;// "double",
	private String isHalfWonLose;// boolean,
	private String isLive;// boolean,
	private List<SboSubRecord> subBet;

	@JsonProperty("MaxWinWithoutActualStake")
	private Double maxWinWithoutActualStake;// "double",

	public List<SboSubRecord> getSubBet() {
		return subBet;
	}

	public String getSettleTime() {
		return settleTime;
	}

	public void setSettleTime(String settleTime) {
		this.settleTime = settleTime;
	}

	public void setSubBet(List<SboSubRecord> subBet) {
		this.subBet = subBet;
	}

	@JsonProperty("Ip")
	private String ip;// "string",

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSportsType() {
		return sportsType;
	}

	public void setSportsType(String sportsType) {
		this.sportsType = sportsType;
	}

	public String getWinLostDate() {
		return winLostDate;
	}

	public void setWinLostDate(String winLostDate) {
		this.winLostDate = winLostDate;
	}

	public Double getWinLost() {
		return winLost;
	}

	public void setWinLost(Double winLost) {
		this.winLost = winLost;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Double getOdds() {
		return odds;
	}

	public void setOdds(Double odds) {
		this.odds = odds;
	}

	public String getOddsStyle() {
		return oddsStyle;
	}

	public void setOddsStyle(String oddsStyle) {
		this.oddsStyle = oddsStyle;
	}

	public Double getStake() {
		return stake;
	}

	public void setStake(Double stake) {
		this.stake = stake;
	}

	public Double getActualStake() {
		return actualStake;
	}

	public void setActualStake(Double actualStake) {
		this.actualStake = actualStake;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getTurnover() {
		return turnover;
	}

	public void setTurnover(Double turnover) {
		this.turnover = turnover;
	}

	public String getIsHalfWonLose() {
		return isHalfWonLose;
	}

	public void setIsHalfWonLose(String isHalfWonLose) {
		this.isHalfWonLose = isHalfWonLose;
	}

	public String getIsLive() {
		return isLive;
	}

	public void setIsLive(String isLive) {
		this.isLive = isLive;
	}

	@JsonProperty("MaxWinWithoutActualStake")
	public Double getMaxWinWithoutActualStake() {
		return maxWinWithoutActualStake;
	}

	public void setMaxWinWithoutActualStake(Double maxWinWithoutActualStake) {
		this.maxWinWithoutActualStake = maxWinWithoutActualStake;
	}

	@JsonProperty("Ip")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
