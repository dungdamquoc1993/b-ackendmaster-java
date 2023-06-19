package com.vinplay.dto.ibc;

import java.io.Serializable;

public class FundTransferReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = -3165041417444390430L;
	private String PlayerName;
	private String OpTransId;
	private Double amount;
	private Integer  Direction;
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
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getDirection() {
		return Direction;
	}
	public void setDirection(Integer direction) {
		Direction = direction;
	}

	


	
	
}
