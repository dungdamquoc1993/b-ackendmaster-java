package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc.BaseReqDto;

import java.io.Serializable;

public class GetMemberBetSettingReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 1946990996420642739L;
	private String PlayerName;

	public String getPlayerName() {
		return PlayerName;
	}

	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}

	
	
}
