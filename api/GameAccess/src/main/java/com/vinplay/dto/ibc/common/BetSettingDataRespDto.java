package com.vinplay.dto.ibc.common;

import java.io.Serializable;

public class BetSettingDataRespDto implements Serializable {
	private static final long serialVersionUID = -1837811222842699771L;
	private String sport_type;
	private Integer min_bet;
	private Integer max_bet;
	private Integer max_bet_per_match;
	private Integer max_bet_per_ball;
	public String getSport_type() {
		return sport_type;
	}
	public void setSport_type(String sport_type) {
		this.sport_type = sport_type;
	}
	public Integer getMin_bet() {
		return min_bet;
	}
	public void setMin_bet(Integer min_bet) {
		this.min_bet = min_bet;
	}
	public Integer getMax_bet() {
		return max_bet;
	}
	public void setMax_bet(Integer max_bet) {
		this.max_bet = max_bet;
	}
	public Integer getMax_bet_per_match() {
		return max_bet_per_match;
	}
	public void setMax_bet_per_match(Integer max_bet_per_match) {
		this.max_bet_per_match = max_bet_per_match;
	}
	public Integer getMax_bet_per_ball() {
		return max_bet_per_ball;
	}
	public void setMax_bet_per_ball(Integer max_bet_per_ball) {
		this.max_bet_per_ball = max_bet_per_ball;
	}
		

}
