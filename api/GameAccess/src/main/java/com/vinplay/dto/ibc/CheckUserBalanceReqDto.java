package com.vinplay.dto.ibc;

import java.io.Serializable;

public class CheckUserBalanceReqDto extends BaseReqDto implements Serializable {

	private static final long serialVersionUID = -7567721278977893407L;
	private String PlayerName;

	public String getPlayerName() {
		return PlayerName;
	}

	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}

}
