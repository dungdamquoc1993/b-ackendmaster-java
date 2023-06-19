package com.vinplay.dto.ag;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "row")
@XmlAccessorType(XmlAccessType.FIELD)
public class AGGamesReportsDetailData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1366883857913233176L;

	@XmlAttribute
	private String billNo;
	
	@XmlAttribute
	private String playName;
	
	@XmlAttribute
	private String gameCode;
	
	@XmlAttribute
	private String netAmount; //payout
	
	@XmlAttribute
	private String betTime;
	
	@XmlAttribute
	private String gameType;
	
	@XmlAttribute
	private String betAmount;
	
	@XmlAttribute
	private String validBetAmount;
	
	@XmlAttribute
	private String flag;
	
	@XmlAttribute
	private String playType;
	
	@XmlAttribute
	private String currency;
	
	@XmlAttribute
	private String tableCode;
	
	@XmlAttribute
	private String recalcuTime; //payout time
	
	@XmlAttribute
	private String beforeCredit;
	
	@XmlAttribute
	private String betIP;
	
	@XmlAttribute
	private String platformType;
	
	@XmlAttribute
	private String remark;
	
	@XmlAttribute
	private String round;
	
	@XmlAttribute
	private String result;
	
	@XmlAttribute
	private String deviceType;

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getPlayName() {
		return playName;
	}

	public void setPlayName(String playName) {
		this.playName = playName;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public String getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}

	public String getBetTime() {
		return betTime;
	}

	public void setBetTime(String betTime) {
		this.betTime = betTime;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(String betAmount) {
		this.betAmount = betAmount;
	}

	public String getValidBetAmount() {
		return validBetAmount;
	}

	public void setValidBetAmount(String validBetAmount) {
		this.validBetAmount = validBetAmount;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTableCode() {
		return tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}

	public String getRecalcuTime() {
		return recalcuTime;
	}

	public void setRecalcuTime(String recalcuTime) {
		this.recalcuTime = recalcuTime;
	}

	public String getBeforeCredit() {
		return beforeCredit;
	}

	public void setBeforeCredit(String beforeCredit) {
		this.beforeCredit = beforeCredit;
	}

	public String getBetIP() {
		return betIP;
	}

	public void setBetIP(String betIP) {
		this.betIP = betIP;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
}
