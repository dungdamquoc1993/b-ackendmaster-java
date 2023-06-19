package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc.BaseRespDto;

import java.io.Serializable;
import java.util.Date;

public class GetBalanceHistoryRespDto extends BaseRespDto implements Serializable {
	private static final long serialVersionUID = 8486839579533943475L;
	private Date TransDate;
	private String Type;
	private String BetType;
	private String Stake;
	private String Winlost;
	private String Odds;
	private String Status;
	private Integer Currency;
	private Integer SportType;
	public Date getTransDate() {
		return TransDate;
	}
	public void setTransDate(Date transDate) {
		TransDate = transDate;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getBetType() {
		return BetType;
	}
	public void setBetType(String betType) {
		BetType = betType;
	}
	public String getStake() {
		return Stake;
	}
	public void setStake(String stake) {
		Stake = stake;
	}
	public String getWinlost() {
		return Winlost;
	}
	public void setWinlost(String winlost) {
		Winlost = winlost;
	}
	public String getOdds() {
		return Odds;
	}
	public void setOdds(String odds) {
		Odds = odds;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public Integer getCurrency() {
		return Currency;
	}
	public void setCurrency(Integer currency) {
		Currency = currency;
	}
	public Integer getSportType() {
		return SportType;
	}
	public void setSportType(Integer sportType) {
		SportType = sportType;
	}
	
	
	
}
