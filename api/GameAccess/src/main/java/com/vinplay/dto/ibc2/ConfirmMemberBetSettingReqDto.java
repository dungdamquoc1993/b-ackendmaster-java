package com.vinplay.dto.ibc2;

import com.vinplay.dto.ibc.BaseReqDto;

import java.io.Serializable;

public class ConfirmMemberBetSettingReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = -6888446477908662296L;
	private String PlayerName;

	public String getPlayerName() {
		return PlayerName;
	}

	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}

	
	
}
