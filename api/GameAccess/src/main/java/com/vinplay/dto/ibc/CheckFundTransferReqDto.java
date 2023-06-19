package com.vinplay.dto.ibc;

import java.io.Serializable;

public class CheckFundTransferReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = -8416705299330825701L;
	private String PlayerName;
	private String OpTransId;
	public String getPlayerName() {
		return PlayerName;
	}
	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}
	public String getOpTransId() {
		return OpTransId;
	}
	public void setOpTransId(String opTransId) {
		OpTransId = opTransId;
	}	
	
}
