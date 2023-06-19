package com.vinplay.dto.ibc;

import java.io.Serializable;

public class GetMemberBetSettingReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 3946544593282627049L;
	private String PlayerName;

	public String getPlayerName() {
		return PlayerName;
	}

	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}

	
	
}
