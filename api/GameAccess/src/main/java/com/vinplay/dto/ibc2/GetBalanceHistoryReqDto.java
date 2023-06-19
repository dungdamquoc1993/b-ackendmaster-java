package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc.BaseReqDto;

import java.io.Serializable;

public class GetBalanceHistoryReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = -4931661814341324961L;
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
