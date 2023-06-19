package com.vinplay.dto.ibc;

import java.io.Serializable;

public class GetBalanceHistoryReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 8106814809369724449L;
	private String PlayerName;
	private String Date;
	public String getPlayerName() {
		return PlayerName;
	}

	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	
	
}
