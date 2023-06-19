package com.vinplay.dto.ibc;

import java.io.Serializable;

public class PrepareMemberBetSettingReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 4343351601883607041L;
	private String PlayerName;
	private String sportType;
	private Integer minBet;
	private Integer maxBet;
	private Integer maxBetPerMatch;
	private Integer maxBetPerBall;

	public String getPlayerName() {
		return PlayerName;
	}

	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}

	public String getSportType() {
		return sportType;
	}

	public void setSportType(String sportType) {
		this.sportType = sportType;
	}

	public Integer getMinBet() {
		return minBet;
	}

	public void setMinBet(Integer minBet) {
		this.minBet = minBet;
	}

	public Integer getMaxBet() {
		return maxBet;
	}

	public void setMaxBet(Integer maxBet) {
		this.maxBet = maxBet;
	}

	public Integer getMaxBetPerMatch() {
		return maxBetPerMatch;
	}

	public void setMaxBetPerMatch(Integer maxBetPerMatch) {
		this.maxBetPerMatch = maxBetPerMatch;
	}

	public Integer getMaxBetPerBall() {
		return maxBetPerBall;
	}

	public void setMaxBetPerBall(Integer maxBetPerBall) {
		this.maxBetPerBall = maxBetPerBall;
	}

}
