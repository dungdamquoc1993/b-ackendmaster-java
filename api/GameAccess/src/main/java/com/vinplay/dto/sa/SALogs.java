package com.vinplay.dto.sa;

import java.io.Serializable;
import java.util.List;

public class SALogs extends BaseResponseDto implements Serializable {

	private static final long serialVersionUID = -6317564087303907602L;
	/**
	 * 
	 */
	private List<SABetDetails> BetDetailList;

	public List<SABetDetails> getBetDetailList() {
		return BetDetailList;
	}

	public void setBetDetailList(List<SABetDetails> betDetailList) {
		BetDetailList = betDetailList;
	}

}
