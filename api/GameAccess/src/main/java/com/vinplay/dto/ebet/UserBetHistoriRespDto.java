package com.vinplay.dto.ebet;

import java.io.Serializable;
import java.util.List;

public class UserBetHistoriRespDto extends BaseRespDto implements Serializable{
	private static final long serialVersionUID = -76198814895564068L;
	private Integer count;
	private  List<BetHistoriesDto> betHistories;
	private Integer remainingVisits;
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public List<BetHistoriesDto> getBetHistories() {
		return betHistories;
	}
	public void setBetHistories(List<BetHistoriesDto> betHistories) {
		this.betHistories = betHistories;
	}
	public Integer getRemainingVisits() {
		return remainingVisits;
	}
	public void setRemainingVisits(Integer remainingVisits) {
		this.remainingVisits = remainingVisits;
	}
}
