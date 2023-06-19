package com.vinplay.dto.ibc;

import java.io.Serializable;

public class KickUserReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = -3979364475108487316L;
	private String PlayerName;

	public String getPlayerName() {
		return PlayerName;
	}

	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}

	
	
}
