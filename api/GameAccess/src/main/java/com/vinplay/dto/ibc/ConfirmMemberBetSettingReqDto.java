package com.vinplay.dto.ibc;

import java.io.Serializable;

public class ConfirmMemberBetSettingReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 5836246579043682423L;
	private String PlayerName;

	public String getPlayerName() {
		return PlayerName;
	}

	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}

	
	
}
